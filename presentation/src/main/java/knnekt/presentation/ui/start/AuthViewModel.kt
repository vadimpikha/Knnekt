package knnekt.presentation.ui.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import knnekt.domain.entity.internal.PhoneAuthStatus
import knnekt.domain.usecase.ConnectycubeSignInUseCase
import knnekt.domain.usecase.ConnectycubeSignInUseCase.*
import knnekt.domain.usecase.FirebaseSignInUseCase
import knnekt.domain.usecase.SendConfirmCodeUseCase
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Common VM for [SendConfirmCodeFragment] and [SignInFragment]
 * So use it as activity VM ( by activityViewModelInstance() )
 */
class AuthViewModel(
    private val connectycubeSignInUseCase: ConnectycubeSignInUseCase
) : ViewModel() {

    val toastEvent = MutableLiveData<Event<String>>()
    val userSignedInEvent = MutableLiveData<Event<Unit>>()

    val login = MutableLiveData("")
    val password = MutableLiveData("")


//    fun sendConfirmCode() {
//
//
//        viewModelScope.launch {
//            sendConfirmCodeUseCase.execute(phone).collect { status ->
//                when (status) {
//                    is PhoneAuthStatus.Completed -> {
//                        connectycubeSignIn(status.token.orEmpty())
//                    }
//                    is PhoneAuthStatus.CodeSent -> {
//                        verificationId = status.verificationId
//                        codeSentEvent.value = Unit.asEvent()
//                    }
//                    is PhoneAuthStatus.Failure -> onError(status.e)
//                }
//            }
//        }
//    }

    fun signIn() {
        val login = login.value!!
        val password = password.value!!
//        viewModelScope.launch {
//            firebaseSignInUseCase.execute(FirebaseSignInUseCase.Params(login, password))
//                .fold(::onError) { token ->
//                    connectycubeSignIn(token.orEmpty())
//                }
//        }

        viewModelScope.launch {
            connectycubeSignInUseCase.execute(Credentials(login, password))
                .fold(::onError) { user ->
                    userSignedInEvent.value = Unit.asEvent()
                }
        }
    }

    private fun onError(t: Throwable) {
        toastEvent.value = (t.message ?: "Error occurred").asEvent()
    }

}