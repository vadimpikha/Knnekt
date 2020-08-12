package knnekt.domain.usecase

import arrow.core.Either
import knnekt.domain.dispatcher.DispatchersProvider
import knnekt.domain.usecase.base.CoroutineUseCase
import knnekt.domain.entity.User
import knnekt.domain.repository.ConnectycubeService

class ConnectycubeSignInUseCase(
    private val connectycubeService: ConnectycubeService,
    dispatchers:DispatchersProvider
) : CoroutineUseCase<Either<Throwable, User>, ConnectycubeSignInUseCase.Credentials>(dispatchers) {


    override suspend fun execute(params: Credentials): Either<Throwable, User> {
        return Either.catch {
            connectycubeService.signIn(params.login, params.password)
        }
    }

    data class Credentials(val login: String, val password: String)
}