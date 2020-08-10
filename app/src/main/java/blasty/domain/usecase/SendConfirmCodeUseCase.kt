package blasty.domain.usecase

import blasty.domain.dispatcher.DispatchersProvider
import blasty.domain.entity.PhoneAuthStatus
import blasty.domain.repository.FirebaseAuthService
import blasty.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

class SendConfirmCodeUseCase(
    private val firebaseAuthService: FirebaseAuthService,
    dispatchers: DispatchersProvider)
    : FlowUseCase<PhoneAuthStatus, String>(dispatchers) {

    override fun execute(params: String): Flow<PhoneAuthStatus> {
        return firebaseAuthService.sendVerificationCode(params)
    }
}