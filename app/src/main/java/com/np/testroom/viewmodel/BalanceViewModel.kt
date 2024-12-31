package com.np.testroom.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.LiveData

class SharedViewModel : ViewModel() {
    // Use a MutableLiveData or any other suitable type to hold your balances data
    val balances = MutableLiveData<List<String>>()

    fun setBalances(balancesList: List<String>) {
        balances.value = balancesList
    }
}
