package knnekt.data.datasource.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import knnekt.data.datasource.db.entity.MessageWithAttachmentsEntity

@Dao
interface MessageWithAttachmentsDao {

    @Transaction
    @Query("SELECT * from messages WHERE chatId = :dialogId ORDER BY dateSent DESC")
    suspend fun getMessagesWithAttachments(dialogId: String): List<MessageWithAttachmentsEntity>

    @Transaction
    @Query("SELECT * FROM messages WHERE chatId = :dialogId ORDER BY dateSent DESC")
    fun postsByDialogId(dialogId: String): PagingSource<Int, MessageWithAttachmentsEntity>
}