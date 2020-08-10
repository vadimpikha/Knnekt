package blasty.domain.repository

import blasty.domain.entity.PhoneAuthStatus
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthService {

    fun isSignedIn(): Boolean
    fun sendVerificationCode(phone: String): Flow<PhoneAuthStatus>

    suspend fun signIn(verificationId: String, smsCode: String): String?

}