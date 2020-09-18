package knnekt.data.mapper

import com.connectycube.chat.model.ConnectycubeDialogType
import knnekt.data.datasource.db.entity.ChatEntity
import knnekt.data.datasource.db.entity.ChatWithPrefsEntity
import knnekt.domain.entity.Chat
import knnekt.domain.mapper.Mapper
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object ChatWithPrefsToPresentationMapper : Mapper<ChatWithPrefsEntity, Chat> {

    override fun convert(obj: ChatWithPrefsEntity): Chat {
        val chat = obj.chat
        val prefs = obj.prefs
        return Chat(
            chat.chatId,
            chat.lastMessage.orEmpty(),
            "",
            chat.photo,
            chat.unreadMessageCount,
            chat.name,
            chat.dialogType,
            getUpdateTime(chat),
            chat.dialogType == ConnectycubeDialogType.PRIVATE.code,
            chat.occupants,
            chat.occupantsCount,
            prefs?.isArchived ?: false,
            prefs?.isMuted ?: false
        )
    }


    private fun getUpdateTime(obj: ChatEntity): String {
        return getPrettyDate(obj.lastMessageDate * 1000)
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