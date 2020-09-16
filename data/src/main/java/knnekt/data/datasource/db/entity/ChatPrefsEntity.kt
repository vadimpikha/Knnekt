package knnekt.data.datasource.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "chats_prefs"
)
data class ChatPrefsEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "chat_id")
    val chatId: String,
    @ColumnInfo(name = "muted")
    val isMuted: Boolean,
    @ColumnInfo(name = "archived")
    val isArchived: Boolean
)