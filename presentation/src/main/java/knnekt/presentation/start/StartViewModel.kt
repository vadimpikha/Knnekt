package knnekt.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import knnekt.domain.users.GetUserSignedStatusUseCase

class StartViewModel(
    getUserSignedStatusUseCase: GetUserSignedStatusUseCase
) : ViewModel() {

    val userLoggedIn = getUserSignedStatusUseCase(Unit).asLiveData()

}