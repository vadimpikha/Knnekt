package knnekt.data.datasource.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val login: String,
    val name: String,
    val phone: String,
    val avatar: String?,
    val lastRequestAt: String,
) {
    override fun toString() = login
}