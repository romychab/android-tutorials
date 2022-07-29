package ua.cn.stu.tests.domain

sealed class Result<T> {

    /**
     * Convert Result<T> into Result<R>.
     */
    fun <R> map(mapper: ((T) -> R)? = null): Result<R> {
        return when (this) {
            is Success<T> -> {
                if (mapper == null) {
                    throw IllegalStateException("Can't map Success<T> result without mapper.")
                } else {
                    Success(mapper(this.value))
                }
            }
            is Error<T> -> Error(this.error)
            is Empty<T> -> Empty()
            is Pending<T> -> Pending()
        }
    }

    fun getValueOrNull(): T? {
        if (this is Success<T>) return this.value
        return null
    }

    fun isFinished() = this is Success<T> || this is Error<T>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (this is Success<*> && other is Success<*>) {
            return this.value == other.value
        } else if (this is Error<*> && other is Error<*>) {
            return this.error == other.error
        }
        return true
    }

    override fun hashCode(): Int {
        if (this is Success<*>) return javaClass.hashCode() +
                31 * this.value.hashCode()
        if (this is Error<*>) return javaClass.hashCode() +
                31 * this.error.hashCode()
        return javaClass.hashCode()
    }

}

class Success<T>(
    val value: T
) : Result<T>()

class Error<T>(
    val error: Throwable
) : Result<T>()

class Empty<T> : Result<T>()

class Pending<T> : Result<T>()