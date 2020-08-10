package blasty.domain.usecase

import arrow.core.Either
import blasty.domain.dispatcher.DispatchersProvider
import blasty.domain.entity.PhoneAuthData
import blasty.domain.repository.FirebaseAuthService
import blasty.domain.usecase.base.CoroutineUseCase
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseSignInUseCase(
    private val firebaseAuthService: FirebaseAuthService,
    dispatchers: DispatchersProvider
) : CoroutineUseCase<Either<Throwable, String?>, PhoneAuthData>(dispatchers) {


    override suspend fun execute(params: PhoneAuthData): Either<Throwable, String?> {
        return Either.catch {
            firebaseAuthService.signIn(params.verificationId, params.smsCode)
        }
    }
}