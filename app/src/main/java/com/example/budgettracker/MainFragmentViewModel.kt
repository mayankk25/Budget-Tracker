package com.example.budgettracker

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.math.BigDecimal

class MainFragmentViewModel: ViewModel() {

    var expenseTotal: Int = 0
    var incomeTotal: Int = 0

    override fun onCleared() {
        super.onCleared()
        Log.i("MainFragmentViewModel", "MainFragmentViewModel destroyed!")
    }
}