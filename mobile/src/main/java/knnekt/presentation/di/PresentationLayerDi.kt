package knnekt.presentation.di

import androidx.lifecycle.ViewModelProvider
import com.connectycube.chat.ConnectycubeChatService
import knnekt.presentation.ui.main.MainViewModel
import knnekt.presentation.ui.main.chats.list.ChatsListViewModel
import knnekt.presentation.ui.main.chats.messages.ChatViewModel
import knnekt.presentation.ui.start.signin.SignInViewModel
import knnekt.presentation.ui.start.StartViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.*

object PresentationLayerDi {

    val viewModelModule = Kodein.Module("viewModelModule") {
        bind<ViewModelProvider.Factory>() with singleton {
            KodeinViewModelFactory(
                this
            )
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
            ChatsListViewModel(instance())
        }
        bindViewModel<ChatViewModel>() with provider {
            ChatViewModel(instance())
        }

        bindViewModel<MainViewModel>() with provider {
            MainViewModel(instance(), instance())
        }
    }
}



