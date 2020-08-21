package knnekt.shared.data.db

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query

@Dao
interface MessageWithAttachmentsDao {
    @Query("SELECT * from messages")
    suspend fun getMessagesWithAttachments(): List<MessageWithAttachmentsEntity>

    @Query("SELECT * FROM messages WHERE dialogId = :dialogId ORDER BY dateSent DESC")
    fun postsByDialogId(dialogId: String): PagingSource<Int, MessageWithAttachmentsEntity>
}