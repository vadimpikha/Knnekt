package knnekt.presentation.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import knnekt.presentation.lifecycle.asEvent
import knnekt.shared.domain.users.CheckUserSignedInUseCase

class StartViewModel(
    private val checkUserSignedInUseCase: CheckUserSignedInUseCase
) : ViewModel() {

    val userLoggedIn = liveData {
        emit(checkUserSignedInUseCase(Unit).asEvent())
    }

}