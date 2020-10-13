package knnekt.data.datasource.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages"
)
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val chatId: String,
    val body: String,
    val readIds: List<Int>,
    val deliveredIds: List<Int>,
    val dateSent: Long,
    val viewsCount: Int?,
    val recipientId: Int?,
    val senderId: Int?,
    val markable: Boolean,
    val isTemp: Boolean
)