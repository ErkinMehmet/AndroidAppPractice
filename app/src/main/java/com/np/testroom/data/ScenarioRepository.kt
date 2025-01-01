package com.np.testroom.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.np.testroom.daos.ScenarioDao
import com.np.testroom.daos.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log


class ScenarioRepository(private val context: Context) {

    // Initialize Room database and DAO
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app-database"
    ).build()

    private val scenarioDao: ScenarioDao = db.scenarioDao()
    private val userDao:UserDao =db.userDao()

    // Insert a new scenario
    suspend fun insertScenario(scenario: Scenario) {
        withContext(Dispatchers.IO) {
            if (userDao.userExists(scenario.userId)) {
                scenarioDao.insertScenario(scenario)
            } else {
                Log.e("ScenarioError", "User with ID ${scenario.userId} does not exist.")
            }
        }
    }

    // Get all scenarios (LiveData allows observation in ViewModel)
    fun getAllScenarios(): LiveData<List<Scenario>> {
        return scenarioDao.getAllScenarios()
    }

    // Optional: Get a specific scenario by ID
    suspend fun getScenarioById(id: Long): Scenario? {
        return withContext(Dispatchers.IO) {
            scenarioDao.getScenarioById(id)
        }
    }

    fun getScenariosByUserId(userId: Long): LiveData<List<Scenario>> {
        return scenarioDao.getScenariosByUserId(userId)
    }
}
