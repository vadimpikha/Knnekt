package blasty.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import blasty.R
import blasty.data.datasource.local.AppDatabase
import blasty.data.datasource.local.LocalChatsDataSourceImpl
import blasty.domain.datasource.local.LocalChatsDataSource
import blasty.data.datasource.remote.RemoteChatsDataSourceImpl
import blasty.data.dispatcher.AndroidDispatchersProvider
import blasty.data.repository.ConnectycubeServiceImpl
import blasty.data.repository.FirebaseAuthServiceImpl
import blasty.domain.datasource.remote.RemoteChatsDataSource
import blasty.domain.repository.ConnectycubeService
import blasty.domain.repository.FirebaseAuthService
import blasty.domain.usecase.*
import blasty.presentation.main.chats.ChatsListViewModel
import blasty.presentation.start.AuthViewModel
import blasty.presentation.start.StartViewModel
import com.google.firebase.auth.FirebaseAuth
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object DiModules {

    val all: List<Kodein.Module> get() = listOf(viewModelModule, repositoryModule, useCaseModule, dataSourceModule, dbModule)

    val viewModelModule = Kodein.Module("viewModelModule") {
        bind<ViewModelProvider.Factory>() with singleton { KodeinViewModelFactory(this) }
        bindViewModel<AuthViewModel>() with provider {
            AuthViewModel(
                instance(),
                instance(),
                instance()
            )
        }
        bindViewModel<StartViewModel>() with provider { StartViewModel(instance()) }
        bindViewModel<ChatsListViewModel>() with provider { ChatsListViewModel(instance()) }
    }

    val repositoryModule = Kodein.Module("repositoryModule") {
        bind<ConnectycubeService>() with provider {
            ConnectycubeServiceImpl(
                instance<Context>().getString(R.string.firebase_project_id)
            )
        }
        bind<FirebaseAuthService>() with provider {
            FirebaseAuthServiceImpl(
                FirebaseAuth.getInstance().apply {
                    useAppLanguage()
                }
            )
        }
    }

    val dataSourceModule = Kodein.Module("dataSourceModule") {
        bind<LocalChatsDataSource>() with provider {
            LocalChatsDataSourceImpl(instance<AppDatabase>().chatDao())
        }

        bind<RemoteChatsDataSource>() with provider {
            RemoteChatsDataSourceImpl()
        }
    }

    val dbModule =  Kodein.Module("dbModule") {
        bind<AppDatabase>() with singleton {
            AppDatabase(instance())
        }
    }

    val useCaseModule = Kodein.Module("useCaseModule") {
        bind<CheckUserSignedInUseCase>() with provider {
            CheckUserSignedInUseCase(instance())
        }

        bind<SendConfirmCodeUseCase>() with provider {
            SendConfirmCodeUseCase(instance(), AndroidDispatchersProvider)
        }

        bind<FirebaseSignInUseCase>() with provider {
            FirebaseSignInUseCase(instance(), AndroidDispatchersProvider)
        }

        bind<ConnectycubeSignInUseCase>() with provider {
            ConnectycubeSignInUseCase(instance(), AndroidDispatchersProvider)
        }
        bind<FetchChatsUseCase>() with provider {
            FetchChatsUseCase(instance(), instance(), AndroidDispatchersProvider)
        }
    }

}



