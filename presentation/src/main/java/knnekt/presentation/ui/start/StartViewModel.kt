package knnekt.presentation.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import knnekt.domain.usecase.CheckUserSignedInUseCase
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent

class StartViewModel(
    private val checkUserSignedInUseCase: CheckUserSignedInUseCase
) : ViewModel() {

    private val _userLoggedIn = MutableLiveData<Event<Boolean>>()
    val userLoggedIn: LiveData<Event<Boolean>> = _userLoggedIn

    init {
        checkUserLoggedIn()
    }

    fun checkUserLoggedIn() {
        _userLoggedIn.value = checkUserSignedInUseCase.execute().asEvent()
    }

}