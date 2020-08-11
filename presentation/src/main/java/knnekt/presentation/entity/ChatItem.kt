package knnekt.presentation.entity

data class ChatItem (
    val id: String,
    val lastMessage: String,
    val lastMessageUser: String,
    val photo: String?,
    val unreadMessageCount: String,
    val name: String,
    val type: Int,
    val updatedAt: String,
    val isPrivate: Boolean
)