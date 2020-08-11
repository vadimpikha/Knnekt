package knnekt.domain.entity

import java.util.*

data class Chat (
    val id: String,
    val lastMessage: String?,
    val lastMessageDateSent: Long,
    val lastMessageUserId: Int?,
    val photo: String?,
    val userId: Int,
    val roomJid: String?,
    val unreadMessageCount: Int?,
    val name: String,
    val type: Int,
    val description: String?,
    val occupantsCount: Int?,
    val occupants: List<Int>,
    val pinnedMessagesIds: List<String>,
    val adminsIds: List<Int>,
    val createdAt: Date,
    val updatedAt: Date
)