package knnekt.data.repository

import knnekt.domain.repository.FirebaseAuthService
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import knnekt.domain.entity.internal.PhoneAuthStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class FirebaseAuthServiceImpl(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthService {

    private val phoneAuth = PhoneAuthProvider.getInstance(firebaseAuth)

    override fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun sendVerificationCode(phone: String) = callbackFlow<PhoneAuthStatus> {

        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                sendBlocking(PhoneAuthStatus.Completed(credential.smsCode))
                channel.close()
            }

            override fun onVerificationFailed(ex: FirebaseException) {
                sendBlocking(PhoneAuthStatus.Failure(ex))
                channel.close()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                sendBlocking(PhoneAuthStatus.CodeSent(verificationId))
            }
        }

        phoneAuth.verifyPhoneNumber(
            phone,
            60,
            TimeUnit.SECONDS,
            Executors.newSingleThreadExecutor(),
            callback
        )

        awaitClose { }
    }

    override suspend fun signIn(verificationId: String, smsCode: String): String? {
        val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
        val user = firebaseAuth.signInWithCredential(credential).await().user ?: return null
        return user.getIdToken(true).await()?.token
    }
}