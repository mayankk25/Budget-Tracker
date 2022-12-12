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
import com.example.budgettracker.databinding.AddIncomeFragmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class AddIncomeFragment: Fragment() {

    private val TAG = "AddIncomeFragment"
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: AddIncomeFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.add_income_fragment, container, false)

        auth = Firebase.auth

        binding.incomeSubmitButton.setOnClickListener{
            val title = binding.incomeTitle.editText?.text.toString()
            val amount = binding.incomeAmount.editText?.text.toString()
            val category = binding.incomeMenu.editText?.text.toString()
            val date = binding.incomeDate.editText?.text.toString()
            if (title != "" && amount != "" && category != "" && date != "") {
                addIncome(binding, auth)
            } else {
                if (title == "") {
                    binding.incomeTitle.isErrorEnabled = true
                    binding.incomeTitle.error = getString(R.string.error)
                } else { binding.incomeTitle.isErrorEnabled = false }
                if (amount == "") {
                    binding.incomeAmount.isErrorEnabled = true
                    binding.incomeAmount.error = getString(R.string.error)
                } else { binding.incomeAmount.isErrorEnabled = false }
                if (category == "") {
                    binding.incomeMenu.isErrorEnabled = true
                    binding.incomeMenu.error = getString(R.string.error)
                } else { binding.incomeMenu.isErrorEnabled = false }
                if (date == "") {
                    binding.incomeDate.isErrorEnabled = true
                    binding.incomeDate.error = getString(R.string.error)
                } else { binding.incomeDate.isErrorEnabled = false }
            }
        }

        val items = arrayOf("Salary", "Bonus", "Allowance", "Other")
        (binding.incomeMenu.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(items)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        binding.incomeDateInput.setOnClickListener {
            datePicker.show(childFragmentManager,"DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val editable = Editable.Factory.getInstance().newEditable(format.format(datePicker.selection))
            binding.incomeDate.editText?.text = editable
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

    fun addIncome(binding: AddIncomeFragmentBinding, auth: FirebaseAuth) {
        val incomeData = Transaction(
            "${auth.currentUser?.uid}",
            "${binding.incomeTitle.editText?.text.toString()}",
            binding.incomeAmount.editText?.text.toString().toInt(), //amount
            "${binding.incomeMenu.editText?.text.toString()}",
            "${binding.incomeDate.editText?.text.toString()}",
            "Income"
        )

        db.collection("transactions")
            .add(incomeData)
            .addOnSuccessListener { documentReference ->
                Log.i(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(context, "Added income.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(AddTransactionFragmentDirections.actionAddTransactionFragmentToMainFragment())
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "Error adding document", e)
                Toast.makeText(context, "Could not add income", Toast.LENGTH_SHORT).show()
            }
    }

}