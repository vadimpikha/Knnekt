package knnekt.shared.domain.users

import knnekt.shared.domain.UseCase
import knnekt.shared.domain.repository.ConnectycubeService


class CheckUserSignedInUseCase(
    private val connectycubeService: ConnectycubeService
) : UseCase<Unit, Boolean>() {

    override fun execute(parameters: Unit) = connectycubeService.isSignedIn()

}