package knnekt.domain.usecase

import knnekt.domain.repository.ConnectycubeService
import knnekt.domain.repository.FirebaseAuthService
import knnekt.domain.usecase.base.UseCase

class CheckUserSignedInUseCase(
    private val connectycubeService: ConnectycubeService
) : UseCase<Boolean, Unit>() {

    override fun execute(params: Unit) = connectycubeService.isSignedIn()

}