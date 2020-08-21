package knnekt.shared.data.lifecycle

import androidx.lifecycle.*
import com.connectycube.chat.ConnectycubeChatService
import timber.log.Timber


class ChatAppLifecycleObserver : DefaultLifecycleObserver {

    fun registeredObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun onLoggedIn() {
        Timber.d("connected")
        if (ProcessLifecycleOwner.get().lifecycle.currentState == Lifecycle.State.RESUMED) {
            Timber.d("connected enterActiveState")
            ConnectycubeChatService.getInstance().enterActiveState()
        }
    }

    fun unregisteredObserver() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        if (ConnectycubeChatService.getInstance().isLoggedIn) {
            Timber.d("onStart enterActiveState")
            ConnectycubeChatService.getInstance().enterActiveState()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        if (ConnectycubeChatService.getInstance().isLoggedIn) {
            Timber.d("onStop enterInactiveState")
            ConnectycubeChatService.getInstance().enterInactiveState()
        }
    }
}