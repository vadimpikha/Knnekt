package knnekt.presentation.ui.start.signin

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import knnekt.domain.usecase.ConnectycubeSignInUseCase
import knnekt.domain.usecase.ConnectycubeSignInUseCase.*
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import kotlinx.coroutines.launch

/**
 * So use it as activity VM ( by activityViewModelInstance() )
 */
class SignInViewModel(
    private val signInUseCase: ConnectycubeSignInUseCase
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
            signInUseCase.execute(Credentials(login, password))
                .fold(::onError) {
                    userSignedInEvent.value = Unit.asEvent()
                }
        }
    }

    private fun onError(t: Throwable) {
        toastEvent.value = (t.message ?: "Error occurred").asEvent()
    }

}