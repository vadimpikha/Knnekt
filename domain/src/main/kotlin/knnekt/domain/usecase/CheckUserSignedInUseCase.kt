package knnekt.domain.usecase

import knnekt.domain.repository.FirebaseAuthService
import knnekt.domain.usecase.base.UseCase

class CheckUserSignedInUseCase(
    private val firebaseAuthService: FirebaseAuthService
) : UseCase<Boolean, Unit>() {

    override fun execute(params: Unit) = firebaseAuthService.isSignedIn()

}