package com.np.testroom.data
import android.content.Context

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
    suspend fun getUserByUsername(username: String): User? {
        return db.userDao().getUserByUsername(username)
    }
    suspend fun getUserByEmail(email: String): User? {
        return db.userDao().getUserByEmail(email)
    }
}
