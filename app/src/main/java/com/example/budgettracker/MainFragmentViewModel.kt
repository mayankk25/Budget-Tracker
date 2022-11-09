package com.example.budgettracker

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainFragmentViewModel: ViewModel() {

    override fun onCleared() {
        super.onCleared()
        Log.i("MainFragmentViewModel", "MainFragmentViewModel destroyed!")
    }
}