package knnekt.presentation.start.auth

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import knnekt.domain.users.SignInUseCase
import knnekt.domain.util.onError
import knnekt.presentation.util.Event
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    val toastEvent = MutableLiveData<Event<String>>()

    val login = MutableLiveData("")
    val password = MutableLiveData("")
    val noData = MediatorLiveData<Boolean>().apply {
        fun rebind() {
            value = login.value.isNullOrEmpty() || password.value.isNullOrEmpty()
        }
        rebind()
        addSource(login) { rebind() }
        addSource(password) { rebind() }
    }

    fun signIn() {
        val login = login.value!!
        val password = password.value!!

        viewModelScope.launch {
            signInUseCase(SignInUseCase.Credentials(login, password))
                .onError(::showFailure)
        }
    }

    private fun showFailure(t: Throwable) {
        toastEvent.value = Event(t.message ?: "Error occurred")
    }

}