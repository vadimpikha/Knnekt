package knnekt.presentation.util

import knnekt.data.datasource.db.entity.ChatEntity
import knnekt.domain.entity.Chat

val Chat.isArchivedSection: Boolean
    get()  = id == ChatEntity.ARCHIVED_SECTION_ID