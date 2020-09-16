package knnekt.domain.entity

data class Message(
        val id: String,
        val dateSent: String,
        val body: String,
        val readIds: List<Int>,
        val deliveredIds: List<Int>,
        val viewsCount: Int?,
        val recipientId: Int?,
        val senderId: Int?,
        val markable: Boolean,
        val delayed: Boolean,
        val attachments: List<Attachment>,
        val isIncoming: Boolean
)