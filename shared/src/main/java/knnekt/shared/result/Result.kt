package knnekt.shared.result

import androidx.lifecycle.MutableLiveData
import knnekt.shared.result.Result.Success

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
    get() = this is Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Success<T>)?.data ?: fallback
}

val <T> Result<T>.data: T?
    get() = (this as? Success)?.data

val Result<*>.exception: Exception?
    get() = (this as? Result.Error)?.exception

/**
 * Updates value of [liveData] if [Result] is of type [Success]
 */
inline fun <reified T> Result<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
    if (this is Success) {
        liveData.value = data
    }
}


inline fun <reified T> Result<T>.onSuccess(block: (T) -> Unit) = apply {
    if (this is Success) {
        block(data)
    }
}

inline fun <reified T> Result<T>.onError(block: (Exception) -> Unit) = apply {
    if (this is Result.Error) {
        block(exception)
    }
}

inline fun <reified T> Result<T>.throwOnError() = onError { throw  it }
