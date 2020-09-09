package knnekt.presentation.util

import knnekt.shared.data.entity.Chat

val Chat.isArchivedSection: Boolean
    get()  = id == Chat.ARCHIVED_CHAT_ID