package knnekt.shared.data.entity

data class Message (
    val id: String,
    val dateSent: String,
    val body: String,
    val readIds: Collection<Int>?,
    val deliveredIds: Collection<Int>?,
    val viewsCount: Int?,
    val recipientId: Int?,
    val senderId: Int?,
    val markable: Boolean,
    val delayed: Boolean,
    val attachments: Collection<Attachment>?,
    val isIncoming: Boolean
)