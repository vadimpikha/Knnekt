package knnekt.data.repository

import com.connectycube.auth.session.ConnectycubeSessionManager
import knnekt.domain.repository.ConnectycubeService
import knnekt.data.util.await
import com.connectycube.users.ConnectycubeUsers
import com.connectycube.users.model.ConnectycubeUser
import knnekt.data.mapper.Mapper
import knnekt.domain.entity.User

class ConnectycubeServiceImpl(
    private val firebaseProjectId: String,
    private val mapper: Mapper<ConnectycubeUser, User>
) : ConnectycubeService {

    override fun isSignedIn(): Boolean {
        return ConnectycubeSessionManager.getInstance().sessionParameters != null
    }

    override suspend fun signIn(accessToken: String): User {
        val (user, _) = ConnectycubeUsers.signInUsingFirebase(firebaseProjectId, accessToken)
            .await()
        return mapper.convert(user)
    }
}