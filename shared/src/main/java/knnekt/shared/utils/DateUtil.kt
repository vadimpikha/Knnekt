package knnekt.shared.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun getPrettyDate(date: Long): String {
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
