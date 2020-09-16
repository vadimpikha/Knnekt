package knnekt.domain.users

import knnekt.domain.entity.User

interface UserAuthService {

    suspend fun signIn(login: String, password: String): User

    fun isSignedIn(): Boolean

}