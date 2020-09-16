package knnekt.data.datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import knnekt.data.datasource.db.converters.ChatConverters
import knnekt.data.datasource.db.entity.*
import knnekt.data.datasource.db.converters.MessageConverters

/**
 * The Room database for this app
 */
@Database(
    entities = [
        UserEntity::class,
        ChatEntity::class,
        MessageEntity::class,
        AttachmentEntity::class,
        ChatPrefsEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ChatConverters::class,
    MessageConverters::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chatsWithPrefsDao(): ChatWithPrefsDao

    abstract fun chatPrefsDao(): ChatPrefsDao

    abstract fun chatDao(): ChatDao

    abstract fun messageDao(): MessageDao

    abstract fun userDao(): UserDao

    abstract fun attachmentDao(): AttachmentDao

    abstract fun messageWithAttachmentDao(): MessageWithAttachmentsDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
                    .also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "knnekt-db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    suspend fun clearTablesForLogout() {
        chatPrefsDao().clear()
        chatDao().nukeTable()
        messageDao().nukeTable()
        attachmentDao().nukeTable()
    }
}