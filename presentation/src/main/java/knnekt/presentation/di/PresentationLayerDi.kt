package knnekt.presentation.di

import androidx.lifecycle.ViewModelProvider
import knnekt.presentation.ui.main.chats.list.ChatsListViewModel
import knnekt.presentation.ui.start.AuthViewModel
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
        bindViewModel<AuthViewModel>() with provider {
            AuthViewModel(
                instance(),
                instance(),
                instance()
            )
        }
        bindViewModel<StartViewModel>() with provider {
            StartViewModel(instance())
        }
        bindViewModel<ChatsListViewModel>() with provider {
            ChatsListViewModel(instance(), instance())
        }
    }

    fun bindConstants(firebaseProjectId: String) = Kodein.Module("bindConstants") {
        constant(tag = "firebase_project_id") with firebaseProjectId
    }
}



