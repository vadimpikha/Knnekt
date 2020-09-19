package knnekt.data.di

import androidx.preference.PreferenceManager
import knnekt.data.chats.ChatsRepositoryImpl
import knnekt.data.datasource.db.AppDatabase
import knnekt.data.datasource.db.entity.*
import knnekt.data.datasource.prefs.SharedPreferencesDataSource
import knnekt.data.datasource.remote.ChatsRemoteDataSource
import knnekt.data.datasource.remote.ChatsRemoteDataSourceImpl
import knnekt.data.datasource.remote.MessagesRemoteDataSource
import knnekt.data.datasource.remote.MessagesRemoteDataSourceImpl
import knnekt.data.datasource.remote.entity.ChatRemoteEntity
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.data.mapper.*
import knnekt.data.messages.MessagesRepositoryImpl
import knnekt.data.users.UserAuthServiceImpl
import knnekt.domain.chats.ChatsRepository
import knnekt.domain.entity.Chat
import knnekt.domain.entity.Message
import knnekt.domain.mapper.Mapper
import knnekt.domain.messages.MessagesRepository
import knnekt.domain.prefs.PreferencesDataSource
import knnekt.domain.users.UserAuthService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object DataLayerDI {

    val mappersModule = DI.Module("DataLayer.MappersModule") {

        bind<Mapper<ChatWithPrefsEntity, Chat>>() with singleton {
            ChatWithPrefsToPresentationMapper
        }

        bind<Mapper<ChatRemoteEntity, ChatEntity>>() with singleton {
            RemoteChatToEntityMapper
        }

        bind<Mapper<MessageWithAttachmentsEntity, Message>>() with singleton {
            val userId = instance<PreferencesDataSource>().currentUser!!.id
            MessageWithAttachmentToPresentationMapper(userId)
        }
        bind<Mapper<MessageRemoteEntity, List<AttachmentEntity>>>() with singleton {
            RemoteMessageAttachmentMapper
        }
        bind<Mapper<MessageRemoteEntity, MessageEntity>>() with singleton {
            RemoteMessageToEntityMapper
        }
    }

    val repositoryModule = DI.Module("DataLayer.RepositoryModule") {

        bind<UserAuthService>() with singleton {
            UserAuthServiceImpl(instance())
        }

        bind<ChatsRepository>() with singleton {
            ChatsRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<MessagesRepository>() with singleton {
            MessagesRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
    }

    val dataSourceModule = DI.Module("DataLayer.DataSourceModule") {

        bind<PreferencesDataSource>() with singleton {
            SharedPreferencesDataSource(
                PreferenceManager.getDefaultSharedPreferences(instance())
            )
        }

        bind<MessagesRemoteDataSource>() with singleton {
            MessagesRemoteDataSourceImpl()
        }

        bind<ChatsRemoteDataSource>() with singleton {
            ChatsRemoteDataSourceImpl()
        }

        bind() from singleton { AppDatabase(instance()) }
    }

}