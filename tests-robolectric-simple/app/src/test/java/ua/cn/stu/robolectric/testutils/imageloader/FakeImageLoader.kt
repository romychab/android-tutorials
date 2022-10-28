package ua.cn.stu.robolectric.testutils.imageloader

import coil.ComponentRegistry
import coil.ImageLoader
import coil.decode.DataSource
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

/**
 * Fake image loader which doesn't load images by URL but
 * creates a stub [FakeImageLoaderDrawable] instead.
 */
class FakeImageLoader : ImageLoader {

    override val components = ComponentRegistry()
    override val defaults = DefaultRequestOptions()
    override val diskCache: DiskCache? = null
    override val memoryCache: MemoryCache? = null

    override fun enqueue(request: ImageRequest): Disposable {
        val url = request.data.toString()
        request.target?.onStart(request.placeholder)
        val drawable = createDrawable(url)
        request.target?.onSuccess(drawable)
        return object : Disposable {
            override val isDisposed: Boolean = true
            override val job: Deferred<ImageResult>
                get() = CompletableDeferred(newResult(request, url))
            override fun dispose() {}
        }
    }

    override suspend fun execute(request: ImageRequest): ImageResult {
        val url = request.data.toString()
        return newResult(request, url)
    }

    override fun newBuilder(): ImageLoader.Builder {
        throw UnsupportedOperationException()
    }

    override fun shutdown() {
    }

    private fun newResult(request: ImageRequest, url: String): SuccessResult {
        return SuccessResult(
            drawable = createDrawable(url),
            request = request,
            dataSource = DataSource.MEMORY_CACHE
        )
    }

    companion object {
        fun createDrawable(url: String) = FakeImageLoaderDrawable(url)
    }

}