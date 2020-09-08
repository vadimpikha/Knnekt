package knnekt.shared.data.db

import androidx.paging.PagingSource
import androidx.room.*
import knnekt.shared.data.db.ChatEntity
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the Chat class.
 */
@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY lastMessageDateSent DESC")
    suspend fun getChatsSync(): List<ChatEntity>

    @Query("SELECT * FROM chats ORDER BY lastMessageDateSent DESC")
    fun getChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats ORDER BY lastMessageDateSent DESC")
    fun getChatsPaging(): PagingSource<Int, ChatEntity>

    @Query("SELECT * FROM chats WHERE chat_id = :chatId")
    suspend fun getChat(chatId: String?): ChatEntity?

    /**
     * update or insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(chat: ChatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<ChatEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfAbsent(vararg chats: ChatEntity)

    @Query("SELECT * FROM chats LIMIT :limit OFFSET :offset")
    fun getChatsByPage(limit: Int, offset: Int): Flow<List<ChatEntity>>

    @Query("DELETE FROM chats")
    suspend fun nukeTable()

    @Query("DELETE FROM chats WHERE chat_id in (:dialogsIds)")
    fun deleteChatsByIds(vararg dialogsIds: String?)

    @Delete
    fun deleteChat(vararg chats: ChatEntity)
}