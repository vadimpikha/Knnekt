package knnekt.shared.data.db

import androidx.room.Embedded
import androidx.room.Relation


data class ChatWithPrefsEntity(
    @Embedded
    val chat: ChatEntity,
    @Relation(
        parentColumn = "chat_id",
        entityColumn = "chat_id"
    )
    val prefs: ChatPrefsEntity?
)