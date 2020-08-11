package knnekt.presentation.util

import java.text.DateFormat
import java.util.*

fun getPrettyDate(date: Long): String {
    val messageDate = Calendar.getInstance().apply { time = Date(date) }
    val currentDate = Calendar.getInstance().apply { time = Date() }

    val messageDay = messageDate.get(Calendar.DAY_OF_YEAR)
    val currentDay = currentDate.get(Calendar.DAY_OF_YEAR)

    return when (currentDay - messageDay) {
        0 -> DateFormat.getTimeInstance(DateFormat.SHORT).format(Date(date))
        else -> DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
    }
}