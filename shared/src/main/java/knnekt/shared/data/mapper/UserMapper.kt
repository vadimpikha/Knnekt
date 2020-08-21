package knnekt.shared.data.mapper

import com.connectycube.users.model.ConnectycubeUser
import knnekt.shared.data.entity.User

object UserMapper : Mapper<ConnectycubeUser, User> {

    override fun convert(obj: ConnectycubeUser): User {
        return User(
            fullName = obj.fullName,
            login = obj.login,
            phone = obj.phone,
            avatar = obj.avatar,
            id = obj.id,
            lastRequestAt = obj.lastRequestAt,
            password = obj.password
        )
    }

}