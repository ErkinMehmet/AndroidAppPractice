// File: src/main/java/com/np/testroom/dao/ScenarioDao.kt

package com.np.testroom.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.np.testroom.data.Scenario
import androidx.room.OnConflictStrategy
import androidx.lifecycle.LiveData

@Dao
interface ScenarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenario(scenario: Scenario)

    @Update
    suspend fun update(scenario: Scenario)

    @Delete
    suspend fun delete(scenario: Scenario)

    @Query("SELECT * FROM scenarios WHERE user_id = :userId")
    fun getScenariosByUserId(userId: Long): LiveData<List<Scenario>>

    @Query("SELECT * FROM scenarios WHERE id = :id")
    suspend fun getScenarioById(id: Long): Scenario?

    @Query("SELECT * FROM scenarios")
    fun getAllScenarios(): LiveData<List<Scenario>>

    @Query("DELETE FROM scenarios WHERE id = :scenarioId")
    suspend fun deleteScenarioById(scenarioId: Long)
}
