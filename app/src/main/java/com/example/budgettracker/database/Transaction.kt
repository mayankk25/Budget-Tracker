package com.example.budgettracker.database

data class Transaction(
    //var expenseID: Long,
    var userID: String? = null,
    var title: String? = null,
    var amount: Double? = null,
    var category: String? = null,
    var date: String? = null,
    var type: String? = null
    //var date: Long = System.currentTimeMillis()
)