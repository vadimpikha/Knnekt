package knnekt.shared.data.entity

import java.util.*

data class User (
    val id: Int,
    val fullName: String?,
    val login: String,
    val phone: String,
    val avatar: String?,
    val lastRequestAt: Date?,
    val password: String?
)