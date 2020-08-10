package blasty.domain.repository

import com.connectycube.users.model.ConnectycubeUser

interface ConnectycubeService {

    suspend fun signIn(accessToken: String): ConnectycubeUser

}