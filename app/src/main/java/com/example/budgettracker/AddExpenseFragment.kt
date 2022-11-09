package com.example.budgettracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.databinding.AddExpenseFragmentBinding
import com.example.budgettracker.databinding.LoginFragmentBinding
import com.example.budgettracker.databinding.MainFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddExpenseFragment: Fragment() {

    private val TAG = "AddExpenseFragment"
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding: AddExpenseFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.add_expense_fragment, container, false)

        //Initialize Firebase
        auth = Firebase.auth

        binding.submitButton.setOnClickListener{
            addExpense(binding, auth)
        }

        return binding.root
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            currentUser?.reload();
        }
    }

    fun addExpense(binding: AddExpenseFragmentBinding, auth: FirebaseAuth) {
        val expenseData = hashMapOf(
            "title" to binding.expenseTitle.text.toString(),
            "amount" to binding.expenseAmount.text.toString(),
            "description" to binding.expenseDescription.text.toString(),
            "userID" to auth.currentUser?.uid
        )

        db.collection("expenses")
            .add(expenseData)
            .addOnSuccessListener { documentReference ->
                Log.i(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(context, "Added expense.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(AddExpenseFragmentDirections.actionAddExpenseFragment2ToMainFragment())
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "Error adding document", e)
                Toast.makeText(context, "Could not add expense", Toast.LENGTH_SHORT).show()
            }
    }
}