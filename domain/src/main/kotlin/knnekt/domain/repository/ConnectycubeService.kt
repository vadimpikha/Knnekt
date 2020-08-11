package knnekt.domain.repository

import knnekt.domain.entity.User

interface ConnectycubeService {

    suspend fun signIn(accessToken: String): User

    fun isSignedIn(): Boolean

}