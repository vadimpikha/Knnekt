package knnekt.shared.data.di

import android.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import knnekt.shared.data.chats.ChatsRepository
import knnekt.shared.data.chats.ChatsRepositoryImpl
import knnekt.shared.data.connection.ChatConnectionManager
import knnekt.shared.data.db.AppDatabase
import knnekt.shared.data.mapper.RemoteChatToEntityMapper
import knnekt.shared.data.mapper.RemoteMessageToEntityMapper
import knnekt.shared.data.messages.MessagesRepository
import knnekt.shared.data.messages.MessagesRepositoryImpl
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
            ChatsRepositoryImpl(instance<AppDatabase>().chatDao(), RemoteChatToEntityMapper)
        }
        bind<MessagesRepository>() with singleton {
            val messageDao = instance<AppDatabase>().messageDao()
            val attachmentDao = instance<AppDatabase>().attachmentDao()
            val messageWithAttachmentDao = instance<AppDatabase>().messageWithAttachmentDao()
            MessagesRepositoryImpl(
                messageDao,
                attachmentDao,
                messageWithAttachmentDao,
                RemoteMessageToEntityMapper,
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

        bind<AppDatabase>() with singleton { AppDatabase(instance()) }
    }

}