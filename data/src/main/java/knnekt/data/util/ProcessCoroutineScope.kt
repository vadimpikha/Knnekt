package knnekt.data.util

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope

val processScope: CoroutineScope
    get() = ProcessLifecycleOwner.get().lifecycleScope