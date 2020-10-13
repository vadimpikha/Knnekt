package knnekt.data.datasource.db

import androidx.room.*
import knnekt.data.datasource.db.entity.ChatEntity
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the Chat class.
 */
@Dao
interface ChatDao {

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM chats INNER JOIN chats_prefs ON chats_prefs.chat_id = chats.chat_id WHERE chats_prefs.archived = 1")
    fun getArchivedChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats ORDER BY last_message_date_sent DESC")
    suspend fun getChatsSync(): List<ChatEntity>

    @Query("SELECT * FROM chats ORDER BY last_message_date_sent DESC")
    fun getChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats WHERE chat_id = :chatId")
    fun getChatFlow(chatId: String): Flow<ChatEntity>

    @Query("SELECT * FROM chats WHERE chat_id = :chatId")
    suspend fun getChat(chatId: String): ChatEntity

    /**
     * update or insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg chats: ChatEntity)

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