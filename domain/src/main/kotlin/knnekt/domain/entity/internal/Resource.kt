package knnekt.domain.entity.internal

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
sealed class Resource<out T> {

    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    fun onSuccess(block: (T) -> Unit) {
        if (this is Success)
            block(this.data)
        Result
    }

    fun onError(block: (Throwable) -> Unit) {
        if (this is Error)
            block(this.throwable)
    }

    fun onLoading(block: () -> Unit) {
        if (this is Loading)
            block()
    }

    fun getOrNull(): T? {
        return if (this is Success)
            this.data
        else
            null
    }

    companion object {
        fun <T> success(data: T) = Resource.Success(data)

        fun error(throwable: Throwable) = Resource.Error(throwable)

        fun loading() = Resource.Loading
    }
}