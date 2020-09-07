package knnekt.shared.domain.repository

import com.connectycube.auth.session.ConnectycubeSessionManager
import com.connectycube.users.ConnectycubeUsers
import com.connectycube.users.model.ConnectycubeUser
import knnekt.shared.data.util.await

interface ConnectycubeService {

    suspend fun signIn(login: String, password: String): ConnectycubeUser

    fun isSignedIn(): Boolean

}

class ConnectycubeServiceImpl : ConnectycubeService {

    override suspend fun signIn(login: String, password: String): ConnectycubeUser {
        return ConnectycubeUsers.signIn(login, password).await()
    }

    override fun isSignedIn(): Boolean {
        return ConnectycubeSessionManager.getInstance().sessionParameters != null
    }
}