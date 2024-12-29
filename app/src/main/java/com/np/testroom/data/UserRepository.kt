package com.np.testroom.data
import com.np.testroom.dao.UserDao
import android.content.Context
import com.np.testroom.data.AppDatabase

class UserRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    suspend fun getUserById(id: Int): User? {
        return db.userDao().getUserById(id)
    }

    suspend fun insertUser(user: User) {
        db.userDao().insert(user)
    }

    suspend fun getAllUsers(): List<User> {
        return db.userDao().getAllUsers()
    }
}
