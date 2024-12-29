package com.np.testroom.dao

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
}
