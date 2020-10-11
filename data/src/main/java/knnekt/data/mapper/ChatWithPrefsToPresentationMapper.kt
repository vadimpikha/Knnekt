package knnekt.data.mapper

import com.connectycube.chat.model.ConnectycubeDialogType
import knnekt.data.datasource.db.entity.ChatEntity
import knnekt.data.datasource.db.entity.ChatPrefsEntity
import knnekt.data.datasource.db.entity.ChatWithPrefsEntity
import knnekt.domain.entity.Chat
import knnekt.domain.entity.GroupChat
import knnekt.domain.entity.PrivateChat
import knnekt.domain.mapper.Mapper
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ChatWithPrefsToPresentationMapper(
    private val currentUserId: Int
) : Mapper<ChatWithPrefsEntity, Chat> {

    override fun convert(obj: ChatWithPrefsEntity): Chat {
        val chat = obj.chat
        val prefs = obj.prefs
        return when (ConnectycubeDialogType.parseByCode(chat.dialogType)!!) {
            ConnectycubeDialogType.PRIVATE -> createPrivateChat(chat, prefs)
            else -> createGroupChat(chat, prefs)
        }
    }

    private fun createGroupChat(chat: ChatEntity, prefs: ChatPrefsEntity?): GroupChat {
        return GroupChat(
            id = chat.chatId,
            lastMessage = chat.lastMessage.orEmpty(),
            photo = chat.photo,
            unreadMessageCount = chat.unreadMessageCount,
            name = chat.name,
            updatedAt = getUpdateTime(chat),
            isArchived = prefs?.isArchived ?: false,
            isMuted = prefs?.isMuted ?: false,
            lastMessageUser = "TODO",
            occupants = chat.occupants,
            occupantsCount = chat.occupantsCount
        )
    }

    private fun createPrivateChat(chat: ChatEntity, prefs: ChatPrefsEntity?): PrivateChat {
        return PrivateChat(
            id = chat.chatId,
            lastMessage = chat.lastMessage.orEmpty(),
            photo = chat.photo,
            unreadMessageCount = chat.unreadMessageCount,
            name = chat.name,
            updatedAt = getUpdateTime(chat),
            isArchived = prefs?.isArchived ?: false,
            isMuted = prefs?.isMuted ?: false,
            interlocutorId = chat.occupants.single { it != currentUserId }
        )
    }


    private fun getUpdateTime(obj: ChatEntity): String {
        var updatedAt = obj.lastMessageDate * 1000
        if (updatedAt == 0L) {
            updatedAt = obj.createdAt
        }
        return getPrettyDate(updatedAt)
    }

    private fun getPrettyDate(date: Long): String {
        val messageDate = Calendar.getInstance().apply { time = Date(date) }
        val currentDate = Calendar.getInstance().apply { time = Date() }

        val messageDay = messageDate.get(Calendar.DAY_OF_YEAR)
        val currentDay = currentDate.get(Calendar.DAY_OF_YEAR)

        val messageWeek = messageDate.get(Calendar.WEEK_OF_YEAR)
        val currentWeek = currentDate.get(Calendar.WEEK_OF_YEAR)

        val messageYear = messageDate.get(Calendar.YEAR)
        val currentYear = currentDate.get(Calendar.YEAR)

        val isThisYear = currentYear == messageYear
        val isThisWeek = currentWeek == messageWeek && isThisYear
        val isToday = currentDay == messageDay && isThisYear

        return when {
            isToday -> DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
            isThisWeek -> SimpleDateFormat("EEE", Locale.getDefault()).format(Date(date))
            isThisYear -> mediumDateInstanceWithoutYears.format(date)
            else -> DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
        }
    }

    private val mediumDateInstanceWithoutYears: DateFormat =
        (DateFormat.getDateInstance(DateFormat.MEDIUM) as SimpleDateFormat).apply {
            applyPattern(
                toPattern().replace(
                    "([^\\p{Alpha}']|('[\\p{Alpha}]+'))*y+([^\\p{Alpha}']|('[\\p{Alpha}]+'))*".toRegex(),
                    ""
                )
            )
        }

}