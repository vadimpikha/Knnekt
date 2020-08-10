package blasty.domain.usecase

import arrow.core.Either
import blasty.domain.dispatcher.DispatchersProvider
import blasty.domain.entity.PhoneAuthData
import blasty.domain.repository.ConnectycubeService
import blasty.domain.repository.FirebaseAuthService
import blasty.domain.usecase.base.CoroutineUseCase
import com.connectycube.users.model.ConnectycubeUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class ConnectycubeSignInUseCase(
    private val connectycubeService: ConnectycubeService,
    dispatchers: DispatchersProvider
) : CoroutineUseCase<Either<Throwable, ConnectycubeUser>, String>(dispatchers) {


    override suspend fun execute(params: String): Either<Throwable, ConnectycubeUser> {
        return Either.catch {
            connectycubeService.signIn(params)
        }
    }
}