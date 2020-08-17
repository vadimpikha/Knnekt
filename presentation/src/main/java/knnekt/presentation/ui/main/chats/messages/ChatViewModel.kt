package knnekt.presentation.ui.main.chats.messages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.core.request.RequestGetBuilder
import knnekt.data.util.await
import knnekt.domain.entity.internal.Resource
import knnekt.domain.usecase.GetChatByIdUseCase
import knnekt.presentation.entity.ChatItem
import knnekt.presentation.entity.MessageItem
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import knnekt.presentation.mapper.ChatItemMapper
import knnekt.presentation.mapper.MessageItemMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val getChatByIdUseCase: GetChatByIdUseCase
) : ViewModel() {

    val outgoingMessageText = MutableLiveData("")

    val chat = MutableLiveData<ChatItem>()
    val toast = MutableLiveData<Event<String>>()

    val messages = MutableLiveData<List<MessageItem>>()

    fun initChat(id: String) {
        load(id)
        viewModelScope.launch {
            getChatByIdUseCase.execute(id).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (it.data != null)
                            chat.value = ChatItemMapper.convert(it.data!!)
                    }
                    is Resource.Success -> chat.value = ChatItemMapper.convert(it.data)
                    is Resource.Error -> onError(it.throwable)
                }
            }
        }
    }

    private fun load(id: String) {
        viewModelScope.launch {
            val request = RequestGetBuilder().apply {
                limit = 20
                sortDesc("date_sent")
            }
            val (messages, _) =
                   ConnectycubeRestChatService.getDialogMessages(
                       ConnectycubeChatDialog(id), request
                   ).await()


            this@ChatViewModel.messages.value = messages.map(MessageItemMapper::convert)
        }
    }

    private fun onError(t: Throwable) {
        toast.value = (t.message ?: "Error").asEvent()
    }

}