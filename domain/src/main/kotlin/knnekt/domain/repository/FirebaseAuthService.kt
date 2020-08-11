package knnekt.domain.repository

import knnekt.domain.entity.internal.PhoneAuthStatus
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthService {

    fun isSignedIn(): Boolean

    fun sendVerificationCode(phone: String): Flow<PhoneAuthStatus>

    suspend fun signIn(verificationId: String, smsCode: String): String?

}