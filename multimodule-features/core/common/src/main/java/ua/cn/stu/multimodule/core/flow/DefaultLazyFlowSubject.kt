package ua.cn.stu.multimodule.core.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ua.cn.stu.multimodule.core.Container

class DefaultLazyFlowSubject<T>(
    private var valueLoader: ValueLoader<T>,
    private val dispatcher: CoroutineDispatcher,
    private val globalScope: CoroutineScope,
    private val cacheTimeoutMillis: Long
) : LazyFlowSubject<T> {

    private var count = 0
    private var scope: CoroutineScope? = null
    private var cancellationJob: Job? = null
    private var inputFlow = MutableStateFlow<Value<T>>(Value.LoadValue(valueLoader))
    private val outputFlow = MutableStateFlow<Container<T>>(Container.Pending)

    private val mutex = Mutex()

    override fun listen(): Flow<Container<T>> = callbackFlow {
        synchronized(this@DefaultLazyFlowSubject) {
            onStart()
        }

        val job = scope?.launch {
            outputFlow.collect {
                trySend(it)
            }
        }

        awaitClose {
            synchronized(this@DefaultLazyFlowSubject) {
                onStop(job)
            }
        }
    }

    override suspend fun newLoad(silently: Boolean, valueLoader: ValueLoader<T>?): T = mutex.withLock {
        val completableDeferred = prepareNewLoad(
            createdCompletableDeferred = true,
            silently = silently,
            valueLoader = valueLoader
        )
        completableDeferred!!.await()
    }

    override fun newAsyncLoad(silently: Boolean, valueLoader: ValueLoader<T>?) {
        prepareNewLoad(
            createdCompletableDeferred = false,
            silently = silently,
            valueLoader = valueLoader
        )
    }

    private fun prepareNewLoad(createdCompletableDeferred: Boolean, silently: Boolean, valueLoader: ValueLoader<T>?): CompletableDeferred<T>? {
        val oldLoad = inputFlow.value
        if (oldLoad is Value.LoadValue && oldLoad.completableDeferred?.isActive == true) {
            oldLoad.completableDeferred.cancel()
        }
        if (valueLoader != null) {
            this.valueLoader = valueLoader
        }
        val completableDeferred = if (createdCompletableDeferred) {
            CompletableDeferred<T>()
        } else {
            null
        }
        inputFlow.value = Value.LoadValue(this.valueLoader, silently, completableDeferred)
        return completableDeferred
    }

    override fun updateWith(container: Container<T>) {
        inputFlow.value = Value.InstantValue(container)
    }

    override fun updateWith(updater: (Container<T>) -> Container<T>) {
        val oldValue = outputFlow.value
        inputFlow.value = Value.InstantValue(updater(oldValue))
    }

    private fun onStart() {
        count++
        if (count == 1) {
            cancellationJob?.cancel()
            startLoading()
        }
    }

    private fun onStop(job: Job?) {
        count--
        job?.cancel()
        if (count == 0) {
            cancellationJob = globalScope.launch {
                delay(cacheTimeoutMillis)
                synchronized(this@DefaultLazyFlowSubject) {
                    if (count == 0) { // double check required
                        scope?.cancel()
                        scope = null
                    }
                }
            }
        }
    }

    private fun startLoading() {
        if (scope != null) return

        outputFlow.value = Container.Pending
        scope = CoroutineScope(SupervisorJob() + dispatcher)
        scope?.launch {
            inputFlow
                .collectLatest {
                    when (it) {
                        is Value.InstantValue -> outputFlow.value = it.container
                        is Value.LoadValue -> loadValue(it)
                    }
                }
        }
    }

    private suspend fun loadValue(loadValue: Value.LoadValue<T>) {
        try {
            if (!loadValue.silent) outputFlow.value = Container.Pending
            val value = loadValue.loader()
            outputFlow.value = Container.Success(value)
            mutex.withLock {
                loadValue.completableDeferred?.complete(value)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            outputFlow.value = Container.Error(e)
            mutex.withLock {
                loadValue.completableDeferred?.completeExceptionally(e)
            }
        }
    }

    sealed class Value<T> {
        class InstantValue<T>(val container: Container<T>) : Value<T>()
        class LoadValue<T>(
            val loader: ValueLoader<T>,
            val silent: Boolean = false,
            val completableDeferred: CompletableDeferred<T>? = null
        ) : Value<T>()
    }
}