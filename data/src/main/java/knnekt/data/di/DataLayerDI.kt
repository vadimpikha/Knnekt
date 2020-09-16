package knnekt.data.di

import androidx.preference.PreferenceManager
import knnekt.data.chats.ChatsRepositoryImpl
import knnekt.data.datasource.db.AppDatabase
import knnekt.data.datasource.db.entity.AttachmentEntity
import knnekt.data.datasource.db.entity.MessageEntity
import knnekt.data.datasource.db.entity.MessageWithAttachmentsEntity
import knnekt.data.datasource.prefs.SharedPreferencesDataSource
import knnekt.data.datasource.remote.MessagesRemoteDataSource
import knnekt.data.datasource.remote.MessagesRemoteDataSourceImpl
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.data.mapper.MessageWithAttachmentToPresentationMapper
import knnekt.data.mapper.RemoteMessageAttachmentMapper
import knnekt.data.mapper.RemoteMessageToEntityMapper
import knnekt.data.messages.MessagesRepositoryImpl
import knnekt.domain.chats.ChatsRepository
import knnekt.domain.entity.Message
import knnekt.domain.mapper.Mapper
import knnekt.domain.messages.MessagesRepository
import knnekt.domain.prefs.PreferencesDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object DataLayerDI {

    val mappersModule = DI.Module("DataLayer.MappersModule") {
        bind<Mapper<MessageWithAttachmentsEntity, Message>>() with singleton {
            MessageWithAttachmentToPresentationMapper
        }
        bind<Mapper<MessageRemoteEntity, List<AttachmentEntity>>>() with singleton {
            RemoteMessageAttachmentMapper
        }
        bind<Mapper<MessageRemoteEntity, MessageEntity>>() with singleton {
            RemoteMessageToEntityMapper
        }
    }

    val repositoryModule = DI.Module("DataLayer.RepositoryModule") {

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

        bind() from singleton { AppDatabase(instance()) }
    }

}