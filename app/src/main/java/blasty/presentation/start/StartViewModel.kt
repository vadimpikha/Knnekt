package blasty.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import blasty.domain.usecase.CheckUserSignedInUseCase
import blasty.lifecycle.asEvent
import blasty.presentation.entity.UserStatus

class StartViewModel(private val checkUserSignedInUseCase: CheckUserSignedInUseCase) : ViewModel() {


    val userStatus = liveData {
        val signed = checkUserSignedInUseCase()
        if (signed) {
            emit(UserStatus.SIGNED_IN.asEvent())
        } else {
            emit(UserStatus.SIGNED_OUT.asEvent())
        }
    }


}