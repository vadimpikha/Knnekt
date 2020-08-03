package blasty.domain.repository

import com.connectycube.users.model.ConnectycubeUser

interface ConnectycubeService {

    fun isSignedIn(): Boolean

//    fun getCurrentUser(): ConnectycubeUser?

    suspend fun signIn(accessToken: String): ConnectycubeUser

}