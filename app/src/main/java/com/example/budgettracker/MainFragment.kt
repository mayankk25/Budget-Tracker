package com.example.budgettracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.database.Transaction
import com.example.budgettracker.databinding.MainFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainFragment: Fragment() {
    private val TAG = "MainFragment"

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: MainFragmentViewModel
    private var db = Firebase.firestore

    private lateinit var expenseList: ArrayList<Transaction>
    private lateinit var incomeList: ArrayList<Transaction>
    private lateinit var transactionList: ArrayList<Transaction>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding: MainFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.main_fragment, container, false)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)

        //get data from firestore
        db.collection("users").whereEqualTo("id", currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (doc in it) {
                        binding.userName.text = doc.data.getValue("name").toString()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        binding.addTransactionButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddTransactionFragment())
        }

        db = FirebaseFirestore.getInstance()

        Log.i(TAG, "Before getting expenses")
        expenseList = arrayListOf()
        incomeList = arrayListOf()
        transactionList = arrayListOf()

        //getting all transactions
        db.collection("transactions")
            .orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("userID", currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    viewModel.expenseTotal = 0.0 // adding this to make sure that everytime going back the expenses do not double
                    viewModel.incomeTotal = 0.0
                    for (data in it.documents) {
                        val transaction: Transaction? = data.toObject(Transaction::class.java)
                        if (transaction != null) {
                            transactionList.add(transaction)
                            if (transaction.type == "Expense") {
                                expenseList.add(transaction)
                                viewModel.expenseTotal += transaction.amount!!
                            }
                            if (transaction.type == "Income") {
                                incomeList.add(transaction)
                                viewModel.incomeTotal += transaction.amount!!
                            }
                        }
                    }
                    binding.totalExpenseAmount.text = viewModel.expenseTotal.toString()
                    binding.totalIncomeAmount.text = viewModel.incomeTotal.toString()
                    binding.totalBalanceAmount.text = (viewModel.incomeTotal - viewModel.expenseTotal).toString()
                    binding.budgetList.adapter = context?.let { it1 -> TransactionAdapter(transactionList, it1) }
                }
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "ERROR", e)
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }

        //getting all the expenses
//        db.collection("expenses")
//            .whereEqualTo("userID", currentUser?.uid.toString())
//            .whereEqualTo("type", "Expense")
//            .get()
//            .addOnSuccessListener {
//                if(!it.isEmpty) {
//                    viewModel.expenseTotal = 0.0 // adding this to make sure that everytime going back the expenses do not double
//                    for (data in it.documents) {
//                        val expense: Transaction? = data.toObject(Transaction::class.java)
//                        if (expense != null) {
//                            expenseList.add(expense)
//                            viewModel.expenseTotal += expense.amount!!
//                        }
//                    }
//                    binding.totalExpenseAmount.text = viewModel.expenseTotal.toString()
//                    binding.budgetList.adapter =
//                        context?.let { it1 -> ExpenseAdapter(expenseList, it1) }
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
//            }
//
//        Log.i(TAG, "Before getting income")
//
//        db.collection("income")
//            .whereEqualTo("userID", currentUser?.uid.toString())
//            .get()
//            .addOnSuccessListener {
//                if(!it.isEmpty) {
//                    viewModel.incomeTotal = 0.0 // adding this to make sure that everytime going back the expenses do not double
//                    for (data in it.documents) {
//                        val income: Transaction? = data.toObject(Transaction::class.java)
//                        if (income != null) {
//                            incomeList.add(income)
//                            viewModel.incomeTotal += income.amount!!
//                        }
//                    }
//                    binding.totalIncomeAmount.text = viewModel.incomeTotal.toString()
//                    //binding.budgetList.adapter = context?.let { it1 -> ExpenseAdapter(incomeList, it1) }
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
//            }

        return binding.root
    }

}