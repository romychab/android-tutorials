package ua.cn.stu.foundation.model

typealias Mapper<Input, Output> = (Input) -> Output

/**
 * Base class which represents result of some async operation
 */
sealed class Result<T> {

    /**
     * Convert this result of type T into another result of type R:
     * - error result of type T is converted to error result of type R with the same exception
     * - pending result of type T is converted to pending result of type R
     * - success result of type T is converted to success result of type R, where conversion
     *   of ([SuccessResult.data] from T to R is conducted by [mapper]
     */
    fun <R> map(mapper: Mapper<T, R>? = null): Result<R> = when(this) {
        is PendingResult -> PendingResult()
        is ErrorResult -> ErrorResult(this.exception)
        is SuccessResult -> {
            if (mapper == null) throw IllegalArgumentException("Mapper should not be NULL for success result")
            SuccessResult(mapper(this.data))
        }
    }

}

/**
 * Operation has been finished
 */
sealed class FinalResult<T> : Result<T>()

/**
 * Operation is in progress
 */
class PendingResult<T> : Result<T>()

/**
 * Operation has finished successfully
 */
class SuccessResult<T>(
    val data: T
) : FinalResult<T>()

/**
 * Operation has finished with error
 */
class ErrorResult<T>(
    val exception: Exception
) : FinalResult<T>()

/**
 * Get success value of [Result] if it is possible; otherwise return NULL.
 */
fun <T> Result<T>?.takeSuccess(): T? {
    return if (this is SuccessResult)
        this.data
    else
        null
}