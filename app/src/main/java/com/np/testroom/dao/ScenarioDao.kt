// File: src/main/java/com/np/testroom/dao/ScenarioDao.kt

package com.np.testroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.np.testroom.data.Scenario

@Dao
interface ScenarioDao {

    @Insert
    suspend fun insert(scenario: Scenario)

    @Update
    suspend fun update(scenario: Scenario)

    @Delete
    suspend fun delete(scenario: Scenario)

    @Query("SELECT * FROM scenarios WHERE user_id = :userId")
    suspend fun getScenariosByUserId(userId: Long): List<Scenario>

    @Query("SELECT * FROM scenarios WHERE id = :id")
    suspend fun getScenarioById(id: Long): Scenario?
}
