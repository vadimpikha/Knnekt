package knnekt.data.datasource.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import knnekt.domain.entity.Chat
import java.time.LocalDateTime

@Entity(
    tableName = "chats"
)
data class ChatEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "chat_id")
    val chatId: String,
    @ColumnInfo(name = "dialog_type")
    val dialogType: Int,
    @ColumnInfo(name = "last_msg_id")
    val lastMessageId: String,
    @ColumnInfo(name = "last_msg")
    val lastMessage: String,
    @ColumnInfo(name = "last_msg_user_id")
    val lastMessageUserId: Int,
    val photo: String?,
    @ColumnInfo(name = "unread_msg_count")
    val unreadMessageCount: Int,
    val name: String,
    @ColumnInfo(name = "last_message_date_sent")
    val lastMessageDate: Long,
    val occupants: List<Int>,
    val occupantsCount: Int
) {

    companion object {
        const val ARCHIVED_SECTION_ID = "archived_section"

        fun createArchivedChatsSection(chatsSequence: String, unreadChats: Int): ChatEntity {
            return ChatEntity(
                chatId = ARCHIVED_SECTION_ID,
                dialogType = 1,
                lastMessageId = "",
                lastMessage = chatsSequence,
                lastMessageUserId = 0,
                photo = null,
                unreadMessageCount = unreadChats,
                name = "",
                Long.MAX_VALUE,
                emptyList(),
                0
            )
        }
    }

}