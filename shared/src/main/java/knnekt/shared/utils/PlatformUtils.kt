package knnekt.shared.utils

import android.os.Looper

object PlatformUtils {

    fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()

}