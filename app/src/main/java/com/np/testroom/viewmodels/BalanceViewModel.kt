package com.np.testroom.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    // Use a MutableLiveData or any other suitable type to hold your balances data
    val balances = MutableLiveData<List<String>>()
    val dateString=MutableLiveData<String>()

    fun setBalances(balancesList: List<String>,date: String) {
        balances.value = balancesList
        dateString.value=date
    }
}
