package knnekt.domain.usecase

import arrow.core.Either
import knnekt.domain.dispatcher.DispatchersProvider
import knnekt.domain.repository.ConnectycubeService
import knnekt.domain.repository.FirebaseAuthService
import knnekt.domain.usecase.base.CoroutineUseCase


class RefreshSessionUseCase(
    dispatchers: DispatchersProvider,
    private val firebaseAuthService: FirebaseAuthService,
    private val connectycubeService: ConnectycubeService
) : CoroutineUseCase<Either<Throwable, Unit>, Unit>(dispatchers) {

    override suspend fun execute(params: Unit): Either<Throwable, Unit> {
        return Either.catch {
            if (true) {
                val token = firebaseAuthService.getIdTokenForCurrentUser()
                connectycubeService.signIn(token.orEmpty())
            }
        }
    }
}