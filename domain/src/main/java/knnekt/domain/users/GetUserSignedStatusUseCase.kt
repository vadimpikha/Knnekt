package knnekt.domain.users

import knnekt.domain.usecase.FlowUseCase
import knnekt.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.kodein.di.bindings.InstanceBinding

class GetUserSignedStatusUseCase(
    private val authService: UserAuthService,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Boolean>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Boolean> = authService.signedInFlow

}