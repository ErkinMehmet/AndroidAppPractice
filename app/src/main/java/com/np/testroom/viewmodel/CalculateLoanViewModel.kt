package com.np.testroom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.np.testroom.data.User
import com.np.testroom.data.UserRepository
import com.np.testroom.data.AppDatabase
import kotlinx.coroutines.launch

class CalculateLoanViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository
    val userLiveData: MutableLiveData<User?> = MutableLiveData()
    val usersLiveData: MutableLiveData<List<User>> = MutableLiveData()

    init {
        userRepository = UserRepository(application)
    }

    fun getUserById(id: Int) {
        viewModelScope.launch {
            val user = userRepository.getUserById(id)
            userLiveData.postValue(user)
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
            getAllUsers()  // Refresh the list after adding
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            val users = userRepository.getAllUsers()
            usersLiveData.postValue(users)
        }
    }
    fun test(){
        viewModelScope.launch {
            val users = userRepository.getAllUsers()
        }
    }
}