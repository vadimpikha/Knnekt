package knnekt.data.datasource.remote.entity

data class MessageRemoteEntity (
    val id: String,
    val chatId: String,
    val dateSend: Long,
    val body: String,
    val readIds: List<Int>,
    val deliveredIds: List<Int>,
    val senderId: Int,
    val attachments: List<AttachmentRemoteEntity>,
    val recipientId: Int?,
    val markable: Boolean,
    val viewsCount: Int?
)