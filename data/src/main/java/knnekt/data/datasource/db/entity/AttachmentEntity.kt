package knnekt.data.datasource.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attachments")
data class AttachmentEntity(
    @PrimaryKey
    @ColumnInfo(name = "attachment_id")
    val attachmentId: String,
    val messageId: String,
    val attachmentType: String,
    val url: String
)  {
    override fun toString() = "attachmentId= $attachmentId, url= $url"
}