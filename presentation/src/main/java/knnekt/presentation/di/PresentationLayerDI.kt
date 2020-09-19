package knnekt.presentation.di

import androidx.lifecycle.ViewModelProvider
import knnekt.presentation.auth.SignInViewModel
import knnekt.presentation.chats.ArchivedChatsViewModel
import knnekt.presentation.chats.ChatsListViewModel
import knnekt.presentation.messages.ChatMessagesViewModel
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

        bindViewModel<SignInViewModel>() with provider {
          SignInViewModel(instance())
        }
//        bindViewModel<StartViewModel>() with provider {
//            StartViewModel(instance())
//        }
        bindViewModel<ChatsListViewModel>() with provider {
            ChatsListViewModel(instance(), instance())
        }
        bindViewModel<ChatMessagesViewModel>() with factory { chatId: String ->
            ChatMessagesViewModel(chatId, instance(), instance())
        }

//        bindViewModel<MainViewModel>() with provider {
//            MainViewModel(instance(), instance())
//        }
    }
}



