package knnekt.data.di

import android.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import knnekt.data.datasource.local.AppDatabase
import knnekt.data.datasource.local.LocalChatsDataSourceImpl
import knnekt.data.datasource.remote.RemoteChatsDataSourceImpl
import knnekt.data.dispatcher.AndroidDispatchersProvider
import knnekt.data.mapper.DomainDataChatsMapper
import knnekt.data.mapper.RemoteChatMapper
import knnekt.data.mapper.UserMapper
import knnekt.data.repository.ChatsRepositoryImpl
import knnekt.data.repository.ConnectycubeServiceImpl
import knnekt.data.repository.FirebaseAuthServiceImpl
import knnekt.data.repository.LocalPreferencesRepositoryImpl
import knnekt.domain.datasource.local.LocalChatsDataSource
import knnekt.domain.datasource.remote.RemoteChatsDataSource
import knnekt.domain.dispatcher.DispatchersProvider
import knnekt.domain.repository.ChatsRepository
import knnekt.domain.repository.ConnectycubeService
import knnekt.domain.repository.FirebaseAuthService
import knnekt.domain.repository.LocalPreferencesRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object DataLayerDi {

    val repositoryModule = Kodein.Module("repositoryModule") {
        bind<ConnectycubeService>() with provider {
            ConnectycubeServiceImpl(instance("firebase_project_id"), UserMapper)
        }
        bind<FirebaseAuthService>() with provider {
            FirebaseAuthServiceImpl(
                FirebaseAuth.getInstance().apply {
                    useAppLanguage()
                }
            )
        }
        bind<ChatsRepository>() with provider {
            ChatsRepositoryImpl(instance(), instance())
        }
        bind<LocalPreferencesRepository>() with singleton {
            LocalPreferencesRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(instance()))
        }
    }

    val dispatcherModule = Kodein.Module("dispatcherModule") {
        bind<DispatchersProvider>() with singleton { AndroidDispatchersProvider }
    }

    val dataSourceModule = Kodein.Module("dataSourceModule") {

        bind<AppDatabase>() with singleton { AppDatabase(instance()) }

        bind<LocalChatsDataSource>() with provider {
            LocalChatsDataSourceImpl(instance<AppDatabase>().chatDao(), DomainDataChatsMapper)
        }

        bind<RemoteChatsDataSource>() with provider {
            RemoteChatsDataSourceImpl(RemoteChatMapper)
        }
    }
}