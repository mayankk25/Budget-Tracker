package com.example.budgettracker

import android.content.ContentValues.TAG
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
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.database.Expense
import com.example.budgettracker.databinding.MainFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainFragment: Fragment() {
    private val TAG = "MainFragment"

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: MainFragmentViewModel
    private var db = Firebase.firestore

    private lateinit var expenseList: ArrayList<Expense>
    private lateinit var incomeList: ArrayList<Expense>

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

        binding.addExpenseButton.setOnClickListener {
            //findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddExpenseFragment2())
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddTransactionFragment())
        }

        db = FirebaseFirestore.getInstance()

        Log.i(TAG, "Before getting expenses")
        expenseList = arrayListOf()

        //getting all the expenses
        db.collection("expenses")
            .whereEqualTo("userID", currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    viewModel.total = 0.0 // adding this to make sure that everytime going back the expenses do not double
                    for (data in it.documents) {
                        val expense: Expense? = data.toObject(Expense::class.java)
                        if (expense != null) {
                            expenseList.add(expense)
                            viewModel.total += expense.amount!!
                        }
                    }
                    binding.totalExpenseAmount.text = viewModel.total.toString()
                    binding.budgetList.adapter =
                        context?.let { it1 -> ExpenseAdapter(expenseList, it1) }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }

        return binding.root
    }

}