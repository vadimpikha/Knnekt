package knnekt.domain.repository

import knnekt.domain.entity.User

interface ConnectycubeService {

    suspend fun signIn(accessToken: String): User

    suspend fun signIn(login: String, password: String): User

    fun isSignedIn(): Boolean

}