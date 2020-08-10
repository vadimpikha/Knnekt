package blasty.data.repository

import blasty.domain.repository.ConnectycubeService
import blasty.utils.await
import com.connectycube.users.ConnectycubeUsers
import com.connectycube.users.model.ConnectycubeUser

class ConnectycubeServiceImpl(
    private val firebaseProjectId: String
) : ConnectycubeService {

    override suspend fun signIn(accessToken: String): ConnectycubeUser {
        val (user, _) = ConnectycubeUsers.signInUsingFirebase(firebaseProjectId, accessToken)
            .await()
        return user
    }
}