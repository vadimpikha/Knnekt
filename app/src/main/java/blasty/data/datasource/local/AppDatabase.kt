package blasty.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import blasty.R
import blasty.data.datasource.converters.ChatConverters
import blasty.domain.entity.local.Chat

/**
 * The Room database for this app
 */
@Database(entities = [Chat::class], version = 1, exportSchema = false)
@TypeConverters(ChatConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, context.getString(R.string.app_name) + "-db")
                .build()
        }
    }

    fun clearTablesForLogout() {
        chatDao().nukeTable()
    }
}