package knnekt.shared.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the User class.
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY name")
    fun getUsersSync(): List<UserEntity>

    @Query("SELECT * FROM users ORDER BY name")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: Int): Flow<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users WHERE id in (:usersIds)")
    fun getUsersByIds(vararg usersIds: Int): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id in (:usersIds)")
    fun getUsersByIdsSync(vararg usersIds: Int): List<UserEntity>
}