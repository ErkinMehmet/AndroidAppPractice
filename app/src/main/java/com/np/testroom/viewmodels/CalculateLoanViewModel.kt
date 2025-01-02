package com.np.testroom.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.np.testroom.data.Scenario
import com.np.testroom.data.ScenarioRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData

class CalculateLoanViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ScenarioRepository = ScenarioRepository(application)
    //val userLiveData: MutableLiveData<User?> = MutableLiveData()
    //al usersLiveData: MutableLiveData<List<User>> = MutableLiveData()
    fun getScenariosByUserId(userId: Int): LiveData<List<Scenario>> {
        return repository.getScenariosByUserId(userId.toLong())// You can call this inside the ViewModel as needed.
    }

    // Insert scenario into database
    fun addScenario(scenario: Scenario) {
        viewModelScope.launch {
            repository.insertScenario(scenario)
        }
    }

    // Optional: Get a single scenario by ID
    suspend fun getScenarioById(id: Int): Scenario? {
        return repository.getScenarioById(id.toLong())
    }
    fun deleteScenario(scenarioId: Long) {
        viewModelScope.launch {
            repository.deleteScenario(scenarioId)
        }
    }

    fun updateScenario(scenario: Scenario?) {
        if (scenario == null) {
            throw IllegalArgumentException("Scénario ne peut pas être nul.")
        }
        viewModelScope.launch {
            repository.updateScenario(scenario)
        }
    }

}
