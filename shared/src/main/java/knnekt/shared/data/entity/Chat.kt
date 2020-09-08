package knnekt.shared.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    val occupantsCount: Int?
) : Parcelable {


    companion object {
        const val ARCHIVED_CHAT_ID = "archived"
    }
}

