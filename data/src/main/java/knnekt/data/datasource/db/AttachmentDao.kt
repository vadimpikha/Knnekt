package knnekt.data.datasource.db

import androidx.room.*
import knnekt.data.datasource.db.entity.AttachmentEntity


@Dao
interface AttachmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<AttachmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: AttachmentEntity)

    @Query("SELECT * FROM attachments WHERE messageId = :messageId ")
    suspend fun loadItemsByMessageId(messageId: String): List<AttachmentEntity>

    @Query("SELECT * FROM attachments WHERE attachment_id = :attachId ")
    suspend fun loadItemsById(attachId: String): List<AttachmentEntity>

    @Query("DELETE FROM attachments WHERE messageId IN (:messageId)")
    suspend fun deleteByMessageId(vararg messageId: String)

    @Query("DELETE FROM attachments WHERE attachment_id = :attachmentId")
    fun deleteById(attachmentId: String)

    @Delete
    fun delete(posts: List<AttachmentEntity>)

    @Query("DELETE FROM attachments")
    suspend fun nukeTable()
}