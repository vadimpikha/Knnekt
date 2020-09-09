package knnekt.shared.data.di

import android.content.Context
import androidx.preference.PreferenceManager
import knnekt.shared.data.chats.ChatMessagesRemoteSource
import knnekt.shared.data.chats.ChatMessagesRemoteSourceImpl
import knnekt.shared.data.chats.ChatsRepository
import knnekt.shared.data.chats.ChatsRepositoryImpl
import knnekt.shared.data.connection.ChatConnectionManager
import knnekt.shared.data.db.AppDatabase
import knnekt.shared.data.mapper.AttachmentMapper
import knnekt.shared.data.mapper.RemoteChatToEntityMapper
import knnekt.shared.data.mapper.RemoteMessageToEntityMapper
import knnekt.shared.data.messages.MessagesRepository
import knnekt.shared.data.messages.MessagesRepositoryImpl
import knnekt.shared.data.resources.AppResources
import knnekt.shared.data.resources.AppResourcesImpl
import knnekt.shared.domain.repository.ConnectycubeService
import knnekt.shared.domain.repository.ConnectycubeServiceImpl
import knnekt.shared.domain.repository.LocalPreferencesRepository
import knnekt.shared.domain.repository.LocalPreferencesRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object DataLayerDi {

    val repositoryModule = Kodein.Module("repositoryModule") {
        bind<ConnectycubeService>() with singleton {
            ConnectycubeServiceImpl()
        }
        bind<ChatsRepository>() with singleton {
            ChatsRepositoryImpl(instance(), RemoteChatToEntityMapper)
        }
        bind<MessagesRepository>() with singleton {
            MessagesRepositoryImpl(
                instance(),
                instance(),
                RemoteMessageToEntityMapper,
                AttachmentMapper,
                instance<LocalPreferencesRepository>().user!!.id
            )
        }
        bind<LocalPreferencesRepository>() with singleton {
            LocalPreferencesRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(instance()))
        }

        bind() from singleton {
            ChatConnectionManager(instance())
        }
    }

    val dataSourceModule = Kodein.Module("dataSourceModule") {

        bind<AppResources>() with provider {
            AppResourcesImpl(instance<Context>().resources)
        }

        bind<ChatMessagesRemoteSource>() with singleton {
            ChatMessagesRemoteSourceImpl()
        }

        bind<AppDatabase>() with singleton { AppDatabase(instance()) }
    }

}