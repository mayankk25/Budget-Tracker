package com.example.budgettracker

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.database.Transaction
import com.example.budgettracker.databinding.AddExpenseFragmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

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
            val title = binding.expenseTitle.editText?.text.toString()
            val amount = binding.expenseAmount.editText?.text.toString()
            val category = binding.expenseMenu.editText?.text.toString()
            val date = binding.expenseDate.editText?.text.toString()
            if (title != "" && amount != "" && category != "" && date != "") {
                addExpense(binding, auth)
            } else {
                if (title == "") {
                    binding.expenseTitle.isErrorEnabled = true
                    binding.expenseTitle.error = getString(R.string.error)
                } else { binding.expenseTitle.isErrorEnabled = false }
                if (amount == "") {
                    binding.expenseAmount.isErrorEnabled = true
                    binding.expenseAmount.error = getString(R.string.error)
                } else { binding.expenseAmount.isErrorEnabled = false }
                if (category == "") {
                    binding.expenseMenu.isErrorEnabled = true
                    binding.expenseMenu.error = getString(R.string.error)
                } else { binding.expenseMenu.isErrorEnabled = false }
                if (date == "") {
                    binding.expenseDate.isErrorEnabled = true
                    binding.expenseDate.error = getString(R.string.error)
                } else { binding.expenseDate.isErrorEnabled = false }
            }
        }

        val items = arrayOf("Food", "Transportation", "Entertainment", "Education", "Health", "Self-Development", "Other")
        (binding.expenseMenu.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(items)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        binding.expenseDateInput.setOnClickListener {
            datePicker.show(childFragmentManager,"DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val editable = Editable.Factory.getInstance().newEditable(format.format(datePicker.selection))
            binding.expenseDate.editText?.text = editable
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

        val expenseData = Transaction(
            "${auth.currentUser?.uid}",
            "${binding.expenseTitle.editText?.text.toString()}",
            binding.expenseAmount.editText?.text.toString().toInt(), //amount
        "${binding.expenseMenu.editText?.text.toString()}",
            "${binding.expenseDate.editText?.text.toString()}",
            "Expense"
        )

        db.collection("transactions")
            .add(expenseData)
            .addOnSuccessListener { documentReference ->
                Log.i(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(context, "Added expense.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(AddTransactionFragmentDirections.actionAddTransactionFragmentToMainFragment())
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "Error adding document", e)
                Toast.makeText(context, "Could not add expense", Toast.LENGTH_SHORT).show()
            }
    }
}
