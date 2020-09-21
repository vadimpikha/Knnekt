package knnekt.domain.entity

enum class ChatType {
    BROADCAST,
    GROUP,
    PRIVATE,
    PUBLIC
}

sealed class Chat(
    val type: ChatType
) {

    abstract val id: String
    abstract val lastMessage: String
    abstract val photo: String?
    abstract val unreadMessageCount: Int
    abstract val name: String
    abstract val updatedAt: String
    abstract val isArchived: Boolean
    abstract val isMuted: Boolean

}

data class PrivateChat(
    override val id: String,
    override val lastMessage: String,
    override val photo: String?,
    override val unreadMessageCount: Int,
    override val name: String,
    override val updatedAt: String,
    override val isArchived: Boolean,
    override val isMuted: Boolean,
    val interlocutorId: Int
) : Chat(ChatType.PRIVATE)

data class GroupChat(
    override val id: String,
    override val lastMessage: String,
    override val photo: String?,
    override val unreadMessageCount: Int,
    override val name: String,
    override val updatedAt: String,
    override val isArchived: Boolean,
    override val isMuted: Boolean,
    val lastMessageUser: String,
    val occupants: List<Int>,
    val occupantsCount: Int
) : Chat(ChatType.GROUP)


