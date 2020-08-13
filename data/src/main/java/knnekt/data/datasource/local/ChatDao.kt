package knnekt.data.datasource.local

import androidx.room.*
import knnekt.data.entity.local.Chat
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the Chat class.
 */
@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY lastMessageDateSent DESC")
    suspend fun getChatsSync(): List<Chat>

    @Query("SELECT * FROM chats ORDER BY lastMessageDateSent DESC")
    fun getChats(): Flow<List<Chat>>

    @Query("SELECT * FROM chats WHERE chat_id = :chatId")
    fun getChat(chatId: String?): Flow<Chat>

    @Query("SELECT * FROM chats WHERE chat_id = :chatId")
    suspend fun getChatSync(chatId: String): Chat?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: Chat)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(chat: Chat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<Chat>)

    @Query("SELECT * FROM chats LIMIT :limit OFFSET :offset")
    fun getChatsByPage(limit: Int, offset: Int): Flow<List<Chat>>

    @Query("DELETE FROM chats")
    fun nukeTable()

    @Query("DELETE FROM chats WHERE chat_id in (:dialogsIds)")
    fun deleteChatsByIds(vararg dialogsIds: String?)

    @Delete
    fun deleteChat(vararg chats: Chat)
}