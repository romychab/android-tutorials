package ua.cn.stu.recyclerview.tasks

sealed class Result<T> {

    @Suppress("UNCHECKED_CAST")
    fun <R> map(mapper: (T) -> R): Result<R> {
        if (this is SuccessResult) return SuccessResult(mapper(data))
        return this as Result<R>
    }

}

class SuccessResult<T>(
    val data: T
) : Result<T>()

class ErrorResult<T>(
    val error: Throwable
) : Result<T>()

class PendingResult<T> : Result<T>()

class EmptyResult<T> : Result<T>()