package knnekt.presentation.ui.main.chats.messages

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import knnekt.domain.entity.Chat
import knnekt.domain.entity.internal.Resource
import knnekt.domain.usecase.GetChatByIdUseCase
import knnekt.presentation.entity.ChatItem
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import knnekt.presentation.mapper.ChatItemMapper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewModel(
    private val getChatByIdUseCase: GetChatByIdUseCase
) : ViewModel() {

    val chat = MutableLiveData<ChatItem>()

    val toast = MutableLiveData<Event<String>>()

    fun initChat(id: String) {
        viewModelScope.launch {
            getChatByIdUseCase.execute(id).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (it.data != null)
                            chat.value = ChatItemMapper.convert(it.data!!)
                    }
                    is Resource.Success ->  chat.value =  ChatItemMapper.convert(it.data)
                    is Resource.Error -> onError(it.throwable)
                }
            }
        }
    }

    private fun onError(t: Throwable) {
        toast.value = (t.message ?: "Error").asEvent()
    }

}