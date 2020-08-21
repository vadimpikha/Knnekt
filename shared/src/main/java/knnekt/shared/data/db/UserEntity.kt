package knnekt.shared.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.connectycube.users.model.ConnectycubeUser

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val userId: Int,
    val login: String,
    val name: String,
    val conUser: ConnectycubeUser
) {
    override fun toString() = login
}