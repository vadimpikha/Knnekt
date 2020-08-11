package knnekt.presentation.ui.main.chats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import knnekt.domain.entity.Chat
import knnekt.domain.entity.internal.Resource
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatsListViewModel(
    private val fetchChatsUseCase: knnekt.domain.usecase.FetchChatsUseCase
) : ViewModel() {

    val chats = MutableLiveData<List<Chat>>()
    val toastEvent = MutableLiveData<Event<String>>()

    init {
        viewModelScope.launch {
            fetchChatsUseCase.execute(knnekt.domain.usecase.FetchChatsUseCase.Params(20, 0)).collect { resource ->
                when (resource) {
                    Resource.Loading -> {}
                    is Resource.Success -> chats.value = resource.data
                    is Resource.Error -> onError(resource.throwable)
                }
            }
        }
    }

    private fun onError(t: Throwable) {
        toastEvent.value = (t.message ?: "Error occurred").asEvent()
    }
}