package com.example.budgettracker.database

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import java.math.BigDecimal
import java.io.Serializable

data class Transaction(
    var userID: String? = null,
    var title: String? = null,
    var amount: Int? = null,
    var category: String? = null,
    var date: String? = null,
    var type: String? = null,
    @DocumentId
    var transactionID: String? = null
): Serializable