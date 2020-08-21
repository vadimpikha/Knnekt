package knnekt.shared.domain.usecase

import com.connectycube.users.model.ConnectycubeUser
import knnekt.shared.data.entity.User
import knnekt.shared.data.mapper.Mapper
import knnekt.shared.domain.CoroutineUseCase
import knnekt.shared.domain.repository.ConnectycubeService
import knnekt.shared.domain.repository.LocalPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher


class ConnectycubeSignInUseCase(
    private val connectycubeService: ConnectycubeService,
    private val localPrefs: LocalPreferencesRepository,
    private val mapper: Mapper<ConnectycubeUser, User>,
    dispatcher: CoroutineDispatcher?
) : CoroutineUseCase<ConnectycubeSignInUseCase.Credentials, User>(dispatcher) {


    override suspend fun execute(parameters: Credentials): User {
        val user = connectycubeService.signIn(parameters.login, parameters.password).apply {
            password = parameters.password
        }
        val mappedUser = mapper.convert(user)
        localPrefs.user = mappedUser
        return mappedUser
    }

    data class Credentials(val login: String, val password: String)
}