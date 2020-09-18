package knnekt.data.util

import android.os.Bundle
import android.os.Looper
import com.connectycube.core.EntityCallback
import com.connectycube.core.exception.ResponseException
import com.connectycube.core.server.Performer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.resume

suspend fun <T> Performer<T>.await(): T {

    if (Looper.myLooper() != Looper.getMainLooper()) {
        throw CancellationException("Performer should be called on main thread")
    }

    if (isCanceled) {
        throw CancellationException("Performer $this was cancelled normally.")
    }

    return suspendCancellableCoroutine { cont ->
        performAsync(object : EntityCallback<T> {
            override fun onSuccess(value: T, bundle: Bundle) = cont.resume(value)
            override fun onError(e: ResponseException) = cont.resumeWithException(e)
        })
    }

}