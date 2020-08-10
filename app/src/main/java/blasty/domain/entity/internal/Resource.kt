package blasty.domain.entity.internal

import blasty.domain.entity.internal.Status.SUCCESS
import blasty.domain.entity.internal.Status.ERROR
import blasty.domain.entity.internal.Status.LOADING

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val throwable: Throwable?,
    val progress: Int?
) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null, null)
        }

        fun error(throwable: Throwable): Resource<Nothing> {
            return Resource(ERROR, null, throwable, null)
        }

        fun loading(): Resource<Nothing> {
            return Resource(LOADING, null, null, null)
        }

        fun <T> loadingProgress(data: T?, progress: Int): Resource<T> {
            return Resource(LOADING, data, null, progress)
        }
    }
}