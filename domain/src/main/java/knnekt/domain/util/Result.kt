package knnekt.domain.util


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    companion object {}

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

inline fun <T> Result.Companion.catch(block: () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error(e)
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data

val Result<*>.exception: Exception?
    get() = (this as? Result.Error)?.exception


inline fun <reified T> Result<T>.onSuccess(block: (T) -> Unit) = apply {
    if (this is Result.Success) {
        block(data)
    }
}

inline fun <reified T> Result<T>.onError(block: (Exception) -> Unit) = apply {
    if (this is Result.Error) {
        block(exception)
    }
}

inline fun <reified T> Result<T>.throwOnError() = onError { throw  it }
