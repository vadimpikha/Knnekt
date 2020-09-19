package knnekt.domain.users

import knnekt.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserAuthService {

    val signedInFlow: Flow<Boolean>

    suspend fun signIn(login: String, password: String): User

    fun isSignedIn(): Boolean

}