package knnekt.data.datasource.prefs

import android.content.SharedPreferences
import com.google.gson.Gson
import knnekt.data.util.StringSerializer
import knnekt.data.util.delegate
import knnekt.domain.entity.User
import knnekt.domain.prefs.PreferencesDataSource

class SharedPreferencesDataSource(
        private val prefs: SharedPreferences
): PreferencesDataSource {

    override var currentUser: User? by prefs.delegate("current_user", null, gsonSerializer())

    private inline fun <reified T> gsonSerializer() = object : StringSerializer<T> {

        private val gson = Gson()

        override fun toString(value: T): String {
            return gson.toJson(value)
        }

        override fun fromString(string: String): T {
            return gson.fromJson<T>(string, T::class.java)
        }
    }

}