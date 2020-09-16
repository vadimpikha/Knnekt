package knnekt.domain.users

import knnekt.domain.usecase.UseCase
import org.kodein.di.bindings.InstanceBinding

class CheckUserSignedInUseCase(
    private val authService: UserAuthService
) : UseCase<Unit, Boolean>() {

    override fun execute(parameters: Unit) = authService.isSignedIn()

}