package com.np.testroom.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.np.testroom.data.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE id = :userId LIMIT 1)")
    suspend fun userExists(userId: Long): Boolean

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}
