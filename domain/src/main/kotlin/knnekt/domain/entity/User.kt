package knnekt.domain.entity

data class User(
    val id: Int,
    val fullName: String?,
    val login: String,
    val phone: String,
    val avatar: String?
)