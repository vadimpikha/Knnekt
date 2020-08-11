package knnekt.domain.usecase

import arrow.core.Either
import knnekt.domain.dispatcher.DispatchersProvider
import knnekt.domain.repository.FirebaseAuthService
import knnekt.domain.usecase.base.CoroutineUseCase

class FirebaseSignInUseCase(
    private val firebaseAuthService: FirebaseAuthService,
    dispatchers: DispatchersProvider
) : CoroutineUseCase<Either<Throwable, String?>, FirebaseSignInUseCase.Params>(dispatchers) {


    override suspend fun execute(params: Params): Either<Throwable, String?> {
        return Either.catch {
            ""
//            firebaseAuthService.signIn(params.verificationId, params.smsCode)
        }
    }

    data class Params (val verificationId: String, val smsCode: String)
}