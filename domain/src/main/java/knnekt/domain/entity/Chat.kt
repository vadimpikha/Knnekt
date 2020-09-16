package knnekt.domain.entity

data class Chat(
    val id: String,
    val lastMessage: String,
    val lastMessageUser: String,
    val photo: String?,
    val unreadMessageCount: String,
    val name: String,
    val type: Int,
    val updatedAt: String,
    val isPrivate: Boolean,
    val occupants: List<Int>,
    val occupantsCount: Int?,
    val isArchived: Boolean,
    val isMuted: Boolean
)