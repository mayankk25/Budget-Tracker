package com.example.budgettracker.database

data class Expense(
    //var expenseID: Long,
    var userID: String? = null,
    var title: String? = null,
    var amount: Double? = null,
    var description: String? = null,
    //var date: Long = System.currentTimeMillis()
)