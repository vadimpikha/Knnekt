package knnekt.presentation.ui.main.chats.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import knnekt.domain.entity.internal.Resource
import knnekt.domain.usecase.FetchChatsUseCase
import knnekt.domain.usecase.RefreshSessionUseCase
import knnekt.presentation.entity.ChatItem
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import knnekt.presentation.mapper.ChatItemMapper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatsListViewModel(
    private val fetchChatsUseCase: FetchChatsUseCase,
    private val refreshSessionUseCase: RefreshSessionUseCase
) : ViewModel() {

    val chats = MutableLiveData<List<ChatItem>>()
    val toastEvent = MutableLiveData<Event<String>>()

    val inProgress = MutableLiveData<Boolean>(false)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            refreshSessionUseCase.execute()
            fetchChatsUseCase.execute(FetchChatsUseCase.Params(20, 0))
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            if (resource.data == null)
                                inProgress.value = true
                            else
                                chats.value = resource.data!!.map(ChatItemMapper::convert)
                        }
                        is Resource.Success -> {
                            inProgress.value = false
                            chats.value = resource.data.map(ChatItemMapper::convert)
                        }
                        is Resource.Error -> {
                            inProgress.value = false
                            onError(resource.throwable)
                        }
                    }
                }
        }
    }

    private fun onError(t: Throwable) {
        toastEvent.value = (t.message ?: "Error occurred").asEvent()
    }
}