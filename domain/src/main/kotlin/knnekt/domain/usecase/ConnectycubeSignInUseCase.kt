package knnekt.domain.usecase

import arrow.core.Either
import arrow.core.orNull
import knnekt.domain.dispatcher.DispatchersProvider
import knnekt.domain.usecase.base.CoroutineUseCase
import knnekt.domain.entity.User
import knnekt.domain.repository.ConnectycubeService
import knnekt.domain.repository.LocalPreferencesRepository

class ConnectycubeSignInUseCase(
    private val connectycubeService: ConnectycubeService,
    private val localPrefs: LocalPreferencesRepository,
    dispatchers:DispatchersProvider
) : CoroutineUseCase<Either<Throwable, User>, ConnectycubeSignInUseCase.Credentials>(dispatchers) {


    override suspend fun execute(params: Credentials): Either<Throwable, User> {
        return Either.catch {
            connectycubeService.signIn(params.login, params.password)
        }.also {
            localPrefs.userId = it.orNull()?.id ?: -1
        }
    }

    data class Credentials(val login: String, val password: String)
}