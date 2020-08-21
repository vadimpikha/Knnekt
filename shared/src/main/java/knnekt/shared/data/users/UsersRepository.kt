package knnekt.shared.data.users

import knnekt.shared.data.db.UserEntity
import knnekt.shared.result.Result

interface UsersRepository {

    suspend fun getUsersByIds(vararg ids: Int): Result<List<UserEntity>>

}

class UsersRepositoryImpl() : UsersRepository {

    override suspend fun getUsersByIds(vararg ids: Int): Result<List<UserEntity>> {
        TODO("Not yet implemented")
    }

}