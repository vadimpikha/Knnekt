package knnekt.presentation.di

import androidx.lifecycle.ViewModelProvider
import knnekt.presentation.ui.main.MainViewModel
import knnekt.presentation.ui.main.chats.list.ChatsListViewModel
import knnekt.presentation.ui.main.chats.list.archived.ArchivedChatsViewModel
import knnekt.presentation.ui.main.chats.messages.ChatViewModel
import knnekt.presentation.ui.start.signin.SignInViewModel
import knnekt.presentation.ui.start.StartViewModel
import knnekt.presentation.viewmodelfactory.ChatViewModelFactory
import knnekt.presentation.viewmodelfactory.KodeinViewModelFactory
import knnekt.shared.data.entity.Chat
import org.kodein.di.Kodein
import org.kodein.di.generic.*

object PresentationLayerDi {

    val viewModelModule = Kodein.Module("viewModelModule") {
        bind<ViewModelProvider.Factory>() with singleton {
            KodeinViewModelFactory(
                this
            )
        }

        bindViewModel<ArchivedChatsViewModel>() with provider {
            ArchivedChatsViewModel(instance(), instance())
        }

        bindViewModel<SignInViewModel>() with provider {
            SignInViewModel(
                instance()
            )
        }
        bindViewModel<StartViewModel>() with provider {
            StartViewModel(instance())
        }
        bindViewModel<ChatsListViewModel>() with provider {
            ChatsListViewModel(instance(), instance())
        }
        bindViewModel<ChatViewModel>() with factory { chat: Chat ->
            ChatViewModel(chat, instance(), instance())
        }

        bindViewModel<MainViewModel>() with provider {
            MainViewModel(instance(), instance())
        }
    }
}



