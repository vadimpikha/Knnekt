package knnekt.presentation.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import knnekt.presentation.ui.main.chats.messages.ChatViewModel
import knnekt.shared.data.entity.Chat
import org.kodein.di.DKodein
import org.kodein.di.generic.instance

@Suppress("UNCHECKED_CAST")
class ChatViewModelFactory(
    private val chat: Chat,
    private val kodein: DKodein
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewModel(
            chat,
            kodein.instance(),
            kodein.instance()
        ) as T
    }

}