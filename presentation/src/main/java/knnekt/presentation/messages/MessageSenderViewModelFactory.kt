package knnekt.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DirectDI
import org.kodein.di.instance

@Suppress("UNCHECKED_CAST")
class MessageSenderViewModelFactory(
    private val chatId: String,
    private val di: DirectDI
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MessageSenderViewModel(
            chatId,
            di.instance()
        ) as T
    }

}