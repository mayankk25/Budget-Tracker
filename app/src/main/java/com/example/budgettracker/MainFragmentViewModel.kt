package com.example.budgettracker

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainFragmentViewModel: ViewModel() {

    var expenseTotal: Double = 0.0
    var incomeTotal: Double = 0.0

    override fun onCleared() {
        super.onCleared()
        Log.i("MainFragmentViewModel", "MainFragmentViewModel destroyed!")
    }
}