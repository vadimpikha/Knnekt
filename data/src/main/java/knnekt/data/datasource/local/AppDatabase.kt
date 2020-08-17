package knnekt.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import knnekt.data.R
import knnekt.data.entity.local.Chat

/**
 * The Room database for this app
 */
@Database(entities = [Chat::class], version = 1, exportSchema = false)
@TypeConverters(knnekt.data.datasource.local.converters.ChatConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao

//    abstract fun messageDao(): MessageDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    )
                        .also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "knnekt-db")
                .build()
        }
    }

    fun clearTablesForLogout() {
        chatDao().nukeTable()
    }
}