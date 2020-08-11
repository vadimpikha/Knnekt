package knnekt.presentation.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import knnekt.domain.usecase.CheckUserSignedInUseCase
import knnekt.domain.usecase.RefreshSessionUseCase
import knnekt.presentation.lifecycle.asEvent
import knnekt.presentation.entity.UserStatus

class StartViewModel(
    private val checkUserSignedInUseCase: CheckUserSignedInUseCase
) : ViewModel() {


    val userStatus = liveData {
        val signed = checkUserSignedInUseCase()
        if (signed) {
            emit(UserStatus.SIGNED_IN.asEvent())
        } else {
            emit(UserStatus.SIGNED_OUT.asEvent())
        }
    }

}