package knnekt.domain.users

import knnekt.domain.usecase.CoroutineUseCase
import knnekt.domain.entity.User
import kotlinx.coroutines.CoroutineDispatcher

class SignInUseCase(
    private val authService: UserAuthService,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<SignInUseCase.Credentials, User>(dispatcher) {


    override suspend fun execute(parameters: Credentials): User {
        return authService.signIn(parameters.login, parameters.password)
    }

    data class Credentials(val login: String, val password: String)
}