package knnekt.domain.prefs

import knnekt.domain.entity.User

interface PreferencesDataSource {

    var currentUser: User?

}