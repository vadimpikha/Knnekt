package knnekt.presentation.di

import androidx.lifecycle.ViewModelProvider
import knnekt.presentation.start.auth.SignInViewModel
import knnekt.presentation.chats.ArchivedChatsViewModel
import knnekt.presentation.chats.ChatsListViewModel
import knnekt.presentation.chats.details.ChatDetailsViewModel
import knnekt.presentation.messages.ChatMessagesViewModel
import knnekt.presentation.start.StartViewModel
import org.kodein.di.*

object PresentationLayerDI {

    val viewModelModule = DI.Module("PresentationLayer.ViewModelModule") {
        bind<ViewModelProvider.Factory>() with singleton {
            KodeinViewModelFactory(
                this
            )
        }

        bindViewModel<ArchivedChatsViewModel>() with provider {
            ArchivedChatsViewModel(instance(), instance())
        }

        bindViewModel<StartViewModel>() with provider {
            StartViewModel(instance())
        }

        bindViewModel<SignInViewModel>() with provider {
          SignInViewModel(instance())
        }

        bindViewModel<ChatsListViewModel>() with provider {
            ChatsListViewModel(instance(), instance())
        }
        bindViewModel<ChatMessagesViewModel>() with factory { chatId: String ->
            ChatMessagesViewModel(chatId, instance(), instance())
        }
        bindViewModel<ChatDetailsViewModel>() with factory { chatId: String ->
            ChatDetailsViewModel(chatId, instance())
        }
    }

}



