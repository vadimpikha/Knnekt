package blasty.domain.usecase

import blasty.domain.repository.ConnectycubeService
import blasty.domain.usecase.base.UseCase

class CheckUserSignedInUseCase(private val connectycubeService: ConnectycubeService): UseCase<Boolean, Unit>() {

    override fun execute(params: Unit) = connectycubeService.isSignedIn()

}