package knnekt.shared.data.db

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MessageWithAttachmentsDao {

    @Transaction
    @Query("SELECT * from messages")
    suspend fun getMessagesWithAttachments(): List<MessageWithAttachmentsEntity>

    @Transaction
    @Query("SELECT * FROM messages WHERE dialogId = :dialogId ORDER BY dateSent DESC")
    fun postsByDialogId(dialogId: String): PagingSource<Int, MessageWithAttachmentsEntity>
}