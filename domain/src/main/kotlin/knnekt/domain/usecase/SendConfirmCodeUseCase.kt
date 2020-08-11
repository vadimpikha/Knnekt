package knnekt.domain.usecase

import knnekt.domain.dispatcher.DispatchersProvider
import knnekt.domain.entity.internal.PhoneAuthStatus
import knnekt.domain.repository.FirebaseAuthService
import knnekt.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

class SendConfirmCodeUseCase(
    private val firebaseAuthService: FirebaseAuthService,
    dispatchers: DispatchersProvider
) : FlowUseCase<PhoneAuthStatus, String>(dispatchers) {

    override fun execute(params: String): Flow<PhoneAuthStatus> {
        return firebaseAuthService.sendVerificationCode(params)
    }
}