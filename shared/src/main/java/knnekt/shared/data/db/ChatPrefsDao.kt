package knnekt.shared.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChatPrefsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg prefs: ChatPrefsEntity)

    @Query("SELECT * FROM chats_prefs WHERE chat_id = :chatId")
    suspend fun findPref(chatId: String): ChatPrefsEntity?

    @Query("DELETE FROM chats_prefs")
    suspend fun clear()

}