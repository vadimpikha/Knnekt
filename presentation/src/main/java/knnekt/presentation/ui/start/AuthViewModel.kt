package knnekt.presentation.ui.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import knnekt.domain.entity.internal.PhoneAuthStatus
import knnekt.domain.usecase.FirebaseSignInUseCase
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Common VM for [SendConfirmCodeFragment] and [SignInFragment]
 * So use it as activity VM ( by activityViewModelInstance() )
 */
class AuthViewModel(
    private val sendConfirmCodeUseCase: knnekt.domain.usecase.SendConfirmCodeUseCase,
    private val firebaseSignInUseCase: knnekt.domain.usecase.FirebaseSignInUseCase,
    private val connectycubeSignInUseCase: knnekt.domain.usecase.ConnectycubeSignInUseCase
) : ViewModel() {

    val userPhone = MutableLiveData("")
    val smsCode = MutableLiveData<String>(null)

    val toastEvent = MutableLiveData<Event<String>>()

    val userSignedInEvent = MutableLiveData<Event<Unit>>()

    val codeSentEvent = MutableLiveData<Event<Unit>>()

    private var verificationId: String = ""

    fun sendConfirmCode() {
        val phone = userPhone.value!!
        if(phone.isEmpty()) return

        viewModelScope.launch {
            sendConfirmCodeUseCase.execute(phone).collect { status ->
                when (status) {
                    is PhoneAuthStatus.Completed -> {
                        smsCode.value = status.confirmCode
                        signIn()
                    }
                    is PhoneAuthStatus.CodeSent -> {
                        verificationId = status.verificationId
                        codeSentEvent.value = Unit.asEvent()
                    }
                    is PhoneAuthStatus.Failure -> onError(status.e)
                }
            }
        }
    }

    fun signIn() {
        val smsCode = smsCode.value.orEmpty()

        viewModelScope.launch {
            firebaseSignInUseCase.execute(FirebaseSignInUseCase.Params(verificationId, smsCode)).fold(::onError) { token ->
                connectycubeSignIn(token.orEmpty())
            }
        }
    }

    suspend fun connectycubeSignIn(token: String) {
        connectycubeSignInUseCase.execute(token).fold(::onError) { user ->
            userSignedInEvent.value = Unit.asEvent()
        }
    }

    private fun onError(t: Throwable) {
        toastEvent.value = (t.message ?: "Error occurred").asEvent()
    }

}