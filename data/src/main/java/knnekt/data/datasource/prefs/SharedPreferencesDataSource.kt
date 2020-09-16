package knnekt.data.datasource.prefs

import android.content.SharedPreferences
import knnekt.data.util.delegate
import knnekt.domain.entity.User
import knnekt.domain.prefs.PreferencesDataSource

class SharedPreferencesDataSource(
        private val prefs: SharedPreferences
): PreferencesDataSource {

    override var currentUser: User? by prefs.delegate("current_user", null)

}