package knnekt.data.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import knnekt.data.datasource.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the User class.
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY name")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: Int): Flow<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: UserEntity)

    @Query("SELECT * FROM users WHERE id in (:usersIds)")
    fun getUsersByIds(vararg usersIds: Int): Flow<List<UserEntity>>
}