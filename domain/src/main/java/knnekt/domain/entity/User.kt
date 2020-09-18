package knnekt.domain.entity

import java.time.LocalDateTime
import java.util.*

data class User (
        val id: Int,
        val name: String?,
        val login: String,
        val phone: String,
        val avatar: String?,
        val lastRequestAt: Date?,
        val password: String?
)