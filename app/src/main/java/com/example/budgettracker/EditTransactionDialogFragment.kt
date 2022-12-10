package com.example.budgettracker

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.database.Transaction
import com.example.budgettracker.databinding.EditTransactionDialogBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.reflect.Array.get
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer
import java.util.*

class EditTransactionDialogFragment(): Fragment() {

    private val TAG = "EditTransactionFragment"

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: EditTransactionDialogBinding = DataBindingUtil.inflate(inflater,
            R.layout.edit_transaction_dialog, container, false)

        auth = Firebase.auth

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigate(EditTransactionDialogFragmentDirections.actionEditTransactionDialogFragment2ToMainFragment())
        }

        val args = EditTransactionDialogFragmentArgs.fromBundle(requireArguments())

        //setting the existing values of the clicked item to default values
        binding.transactionTypeInput.setText(args.clickedItem.type)
        binding.transactionTitleInput.setText(args.clickedItem.title)
        binding.transactionAmountInput.setText(args.clickedItem.amount.toString())
        binding.transactionDateInput.setText(args.clickedItem.date)
        binding.transactionMenuInput.setText(args.clickedItem.category)

        //creating date picker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        binding.transactionDateInput.setOnClickListener {
            datePicker.show(childFragmentManager,"DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            binding.transactionDateInput.setText(format.format(datePicker.selection))
        }

        //current type
        val expenseItems = arrayOf("Food", "Transportation", "Entertainment", "Education", "Health", "Self-Development", "Other")
        val incomeItems = arrayOf("Salary", "Bonus", "Allowance", "Other")

        when (binding.transactionType.editText?.text.toString()) {
            "Expense" -> (binding.transactionMenu.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(expenseItems)
            else -> (binding.transactionMenu.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(incomeItems)
        }

        val typeItems = arrayOf("Expense", "Income")
        (binding.transactionType.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(typeItems)

        binding.saveButton.setOnClickListener {
            val docData = hashMapOf(
                "title" to binding.transactionTitle.editText?.text.toString(),
                "amount" to binding.transactionAmount.editText?.text.toString().toInt(),
                "category" to binding.transactionMenu.editText?.text.toString(),
                "type" to binding.transactionType.editText?.text.toString(),
                "date" to binding.transactionDate.editText?.text.toString(),
                "userID" to auth.currentUser?.uid
            )

            val docRef = db.collection("transactions").document(args.clickedItem.transactionID!!)
            docRef
                .set(docData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Updated Transaction", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener {
                        e -> Log.w(TAG, "Error writing document", e)
                    Toast.makeText(context, "Error updating transaction", Toast.LENGTH_SHORT).show()
                }
        }

        return binding.root
    }

//    private lateinit var binding: EditTransactionDialogBinding
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        // Inflate the layout to use as dialog or embedded fragment
//        binding = DataBindingUtil.inflate(inflater, R.layout.edit_transaction_dialog, container, false)
//
//        binding.topAppBar.setNavigationOnClickListener {
//            //findNavController().navigate(EditTransactionDialogFragmentDirections.actionEditTransactionDialogFragmentToMainFragment())
//            var clickedItem = transactions[position]
//            clickedItem.title = "Clicked"
//            dismiss()
//        }
//
//        return binding.root
//        //return inflater.inflate(R.layout.edit_transaction_dialog, container, false)
//    }
//
//    /** The system calls this only when creating the layout in a dialog. */
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        // The only reason you might override this method when using onCreateView() is
//        // to modify any dialog characteristics. For example, the dialog includes a
//        // title by default, but your custom layout might not need it. So here you can
//        // remove the dialog title, but you must call the superclass to get the Dialog.
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        return dialog
//    }
}