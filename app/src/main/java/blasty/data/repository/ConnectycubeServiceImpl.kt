package blasty.data.repository

import blasty.domain.repository.ConnectycubeService
import blasty.utils.await
import com.connectycube.auth.session.ConnectycubeSessionManager
import com.connectycube.users.ConnectycubeUsers
import com.connectycube.users.model.ConnectycubeUser

class ConnectycubeServiceImpl(
    private val sessionManager: ConnectycubeSessionManager,
    private val firebaseProjectId: String
) : ConnectycubeService {

    override fun isSignedIn(): Boolean {
        return sessionManager.activeSession != null
    }

    override suspend fun signIn(accessToken: String): ConnectycubeUser {
        val (user, _) = ConnectycubeUsers.signInUsingFirebase(firebaseProjectId, accessToken)
            .await()
        return user
    }
}