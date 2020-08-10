package blasty.presentation.main.chats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import blasty.domain.entity.internal.Status
import blasty.domain.usecase.FetchChatsUseCase
import blasty.lifecycle.Event
import blasty.lifecycle.asEvent
import com.connectycube.chat.model.ConnectycubeChatDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatsListViewModel(
    private val fetchChatsUseCase: FetchChatsUseCase
) : ViewModel() {

    val chats = MutableLiveData<List<ConnectycubeChatDialog>>()
    val toastEvent = MutableLiveData<Event<String>>()

    init {
        viewModelScope.launch {
            fetchChatsUseCase.execute(FetchChatsUseCase.Params(20, 0)).collect { resource ->
                when (resource.status) {
                    Status.SUCCESS -> chats.value = resource.data.orEmpty()
                    Status.ERROR -> onError(resource.throwable ?: UnknownError("Error"))
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun onError(t: Throwable) {
        toastEvent.value = (t.message ?: "Error occurred").asEvent()
    }
}