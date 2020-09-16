package knnekt.domain.entity

import java.time.LocalDateTime

data class User (
        val id: Int,
        val name: String?,
        val login: String,
        val phone: String,
        val avatar: String?,
        val lastRequestAt: LocalDateTime?,
        val password: String?
)