package knnekt.data.repository

import android.content.SharedPreferences
import knnekt.data.util.delegate
import knnekt.domain.repository.LocalPreferencesRepository

class LocalPreferencesRepositoryImpl(prefs: SharedPreferences): LocalPreferencesRepository {

    companion object {
        const val KEY_USER_ID = "user-id"
    }

    override var userId: Int by prefs.delegate(KEY_USER_ID, -1)


}