package knnekt.data.datasource.db

import androidx.paging.PagingSource
import androidx.room.*
import knnekt.data.datasource.db.entity.MessageEntity

/**
 * The Data Access Object for the Message class.
 */
@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: MessageEntity)

    @Query("SELECT * FROM messages WHERE id = :id ")
    fun loadItem(id: String): MessageEntity

    @Query("UPDATE messages SET deliveredIds = :userId WHERE id = :id")
    fun updateDeliveredIds(id: String, userId: String): Int

    @Query("SELECT * FROM messages WHERE chatId = :dialogId ORDER BY dateSent DESC")
    fun messagesByDialogIdPaging(dialogId: String): PagingSource<Int, MessageEntity>

    @Query("SELECT * FROM messages WHERE chatId = :dialogId ORDER BY dateSent DESC")
    suspend fun messagesByDialogId(dialogId: String): List<MessageEntity>

    @Query("SELECT id FROM messages WHERE chatId = :dialogId")
    suspend fun messagesIdsByDialogId(dialogId: String): Array<String>

    @Query("DELETE FROM messages WHERE chatId = :dialogId")
    suspend fun deleteByDialogId(dialogId: String)

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteByMessageId(messageId: String)

    @Query("DELETE FROM messages")
    suspend fun nukeTable()

    @Query("UPDATE messages SET isTemp = :temp WHERE id = :id")
    suspend fun updateTemp(id: String, temp: Boolean)
}