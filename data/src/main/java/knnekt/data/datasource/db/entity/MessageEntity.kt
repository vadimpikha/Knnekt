package knnekt.data.datasource.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    ignoredColumns = ["attachments"]
)
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val body: String,
    val readIds: List<Int>,
    val deliveredIds: List<Int>,
    val dateSent: Long,
    val viewsCount: Int?,
    val recipientId: Int?,
    val senderId: Int?,
    val markable: Boolean
) {

    var attachments: List<AttachmentEntity> = emptyList()

}