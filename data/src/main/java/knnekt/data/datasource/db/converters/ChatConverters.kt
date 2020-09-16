package knnekt.data.datasource.db.converters

import androidx.room.TypeConverter

/**
 * Type converters to allow Room to reference complex data types.
 */
class ChatConverters {

    @TypeConverter
    fun fromStringListString(value: String?): List<String>? {
        return value?.split(", ").orEmpty()
    }

    @TypeConverter
    fun toStringListString(list: List<String>?): String? {
        return list?.joinToString(", ").orEmpty()
    }

    @TypeConverter
    fun fromIdsListString(value: String?): List<Int>? {
        return value?.let {
            if (value.isNotEmpty()) value.split(", ").map { it.toInt() } else emptyList()
        }.orEmpty()
    }

    @TypeConverter
    fun toIdsListString(list: List<Int>?): String? {
        return list?.joinToString(", ").orEmpty()
    }
}