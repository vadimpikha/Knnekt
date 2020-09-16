package knnekt.presentation.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, block: (T) -> Unit) {
    observe(owner, EventObserver(block))
}