package knnekt.data.users

import com.connectycube.auth.session.ConnectycubeSessionManager
import com.connectycube.users.ConnectycubeUsers
import com.connectycube.users.model.ConnectycubeUser
import knnekt.data.util.await
import knnekt.domain.entity.User
import knnekt.domain.prefs.PreferencesDataSource
import knnekt.domain.users.UserAuthService

class UserAuthServiceImpl(
    private val preferencesDataSource: PreferencesDataSource
) : UserAuthService {

    override suspend fun signIn(login: String, password: String): User {
        return ConnectycubeUsers.signIn(login, password).await().let { convert(it, password) }
            .also {
                preferencesDataSource.currentUser = it
            }
    }

    override fun isSignedIn(): Boolean {
        return ConnectycubeSessionManager.getInstance().sessionParameters != null
    }

    private fun convert(user: ConnectycubeUser, password: String): User {
        return User(
            user.id,
            user.fullName,
            user.login,
            user.phone,
            user.avatar,
            user.lastRequestAt,
            password
        )
    }

}