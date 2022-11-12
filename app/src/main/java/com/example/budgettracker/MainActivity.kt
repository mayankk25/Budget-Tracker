package com.example.budgettracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val navController = this.findNavController(R.id.myNavHostFragment)
//        NavigationUI.setupActionBarWithNavController(this, navController)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.myNavHostFragment)
//        return navController.navigateUp()
//    }
}