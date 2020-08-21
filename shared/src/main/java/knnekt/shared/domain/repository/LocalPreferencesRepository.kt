package knnekt.shared.domain.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import knnekt.shared.data.entity.User
import knnekt.shared.data.util.StringSerializer
import knnekt.shared.data.util.delegate

interface LocalPreferencesRepository {

    var user: User?

}


class LocalPreferencesRepositoryImpl(prefs: SharedPreferences) : LocalPreferencesRepository {

    override var user: User? by prefs.delegate("current_user", null, gsonSerializer())






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