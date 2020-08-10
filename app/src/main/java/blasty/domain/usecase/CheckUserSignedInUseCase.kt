package blasty.domain.usecase

import blasty.domain.repository.FirebaseAuthService
import blasty.domain.usecase.base.UseCase

class CheckUserSignedInUseCase(private val firebaseAuthService: FirebaseAuthService): UseCase<Boolean, Unit>() {

    override fun execute(params: Unit) = firebaseAuthService.isSignedIn()

}