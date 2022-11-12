package com.example.budgettracker.database

import java.text.SimpleDateFormat

data class Expense(
    //var expenseID: Long,
    var userID: String? = null,
    var title: String? = null,
    var amount: Double? = null,
    var category: String? = null,
    var date: String? = null
    //var date: Long = System.currentTimeMillis()
)