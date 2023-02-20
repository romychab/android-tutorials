package ua.cn.stu.multimodule.core

import kotlinx.coroutines.runBlocking

/**
 * Represents the current status of async fetch/operation.
 * @see Container.Pending
 * @see Container.Error
 * @see Container.Success
 */
sealed class Container<out T> {

    /**
     * Convert the container type to another type.
     */
    fun <R> map(mapper: ((T) -> R)? = null): Container<R> {
        return runBlocking {
            val suspendMapper: (suspend (T) -> R)? = if (mapper == null) {
                null
            } else {
                {
                    mapper(it)
                }
            }
            suspendMap(suspendMapper)
        }
    }

    /**
     * Convert the container type to another type by using a suspend lambda.
     */
    abstract suspend fun <R> suspendMap(mapper: (suspend (T) -> R)? = null): Container<R>

    /**
     * Get the value backed by the container if possible or throw exception.
     */
    abstract fun unwrap(): T

    /**
     * Get the value backed by the container if possible or return null.
     */
    abstract fun getOrNull(): T?

    /**
     * The operation in still in progress.
     */
    object Pending : Container<Nothing>() {

        override suspend fun <R> suspendMap(mapper: (suspend (Nothing) -> R)?): Container<R> {
            return this
        }

        override fun unwrap(): Nothing {
            throw IllegalStateException("Container is Pending")
        }

        override fun getOrNull(): Nothing? {
            return null
        }
    }

    /**
     * The operation has been failed.
     */
    data class Error(
        val exception: Exception
    ) : Container<Nothing>() {

        override suspend fun <R> suspendMap(mapper: (suspend (Nothing) -> R)?): Container<R> {
            return this
        }

        override fun unwrap(): Nothing {
            throw exception
        }

        override fun getOrNull(): Nothing? {
            return null
        }
    }

    /**
     * The operation has been finished with success.
     */
    data class Success<T>(
        val value: T
    ) : Container<T>() {

        override suspend fun <R> suspendMap(mapper: (suspend (T) -> R)?): Container<R> {
            if (mapper == null) throw IllegalStateException("Can't map Container.Success without mapper")
            return try {
                Success(mapper(value))
            } catch (e: Exception) {
                Error(e)
            }
        }

        override fun unwrap(): T {
            return value
        }

        override fun getOrNull(): T? {
            return value
        }
    }

}
