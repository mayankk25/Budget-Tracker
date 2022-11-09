package com.example.budgettracker.database

data class Expense(
    var expenseID: Long,
    var userID: String,
    var amount: Double,
    var title: String,
    var description: String,
    var date: Long = System.currentTimeMillis()
)