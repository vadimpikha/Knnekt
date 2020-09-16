package knnekt.presentation.auth

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import knnekt.domain.users.SignInUseCase
import knnekt.domain.util.onError
import knnekt.domain.util.onSuccess
import knnekt.presentation.util.Event
import knnekt.presentation.util.UnitEvent
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    val toastEvent = MutableLiveData<Event<String>>()
    val userSignedInEvent = MutableLiveData<Event<Unit>>()

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
                .onError (::onError)
                .onSuccess {
                    userSignedInEvent.value = UnitEvent()
                }
        }
    }

    private fun onError(t: Throwable) {
        toastEvent.value = Event(t.message ?: "Error occurred")
    }

}