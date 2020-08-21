package knnekt.shared.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import knnekt.shared.data.db.converters.ChatConverters
import knnekt.shared.data.db.converters.MessageConverters
import knnekt.shared.data.db.converters.UserConverters

/**
 * The Room database for this app
 */
@Database(
    entities = [UserEntity::class, ChatEntity::class, MessageEntity::class, AttachmentEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    UserConverters::class,
    ChatConverters::class,
    MessageConverters::class
)
abstract class AppDatabase : RoomDatabase() {

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
                .build()
        }
    }

    suspend fun clearTablesForLogout() {
        chatDao().nukeTable()
        messageDao().nukeTable()
        attachmentDao().nukeTable()
    }
}