package com.example.budgettracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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
import java.math.BigDecimal

class MainFragment: Fragment(), TransactionAdapter.OnItemClickListener {
    private val TAG = "MainFragment"

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: MainFragmentViewModel
    private var db = Firebase.firestore

    private lateinit var expenseList: ArrayList<Transaction>
    private lateinit var incomeList: ArrayList<Transaction>
    private lateinit var transactionList: ArrayList<Transaction>
    private lateinit var adapter: TransactionAdapter

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


        adapter = context?.let { TransactionAdapter(transactionList, this, it) }!!
        //getting all transactions
        db.collection("transactions")
            .orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("userID", currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    viewModel.expenseTotal = 0 // adding this to make sure that everytime going back the expenses do not double
                    viewModel.incomeTotal = 0
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
                    binding.budgetList.adapter = adapter
                }
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "ERROR", e)
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }


        //Budget Goals Fragment
        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.budget_goals -> {
                    // Handle accelerator icon press
                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToBudgetGoalsFragment2())
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(context, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = transactionList[position]
        //changes the title for the clicked item temporarily. If the main fragment page is shown again, it will be removed
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToEditTransactionDialogFragment2(clickedItem))
        adapter?.notifyItemChanged(position)
    }

//    fun showDialog(transactions: ArrayList<Transaction>, position: Int) {
//        val fragmentManager = requireActivity().supportFragmentManager
//        val newFragment = EditTransactionDialogFragment(transactions, position)
//        if (false) {
//            // The device is using a large layout, so show the fragment as a dialog
//            newFragment.show(fragmentManager, "dialog")
//        } else {
//            // The device is smaller, so show the fragment fullscreen
//            val transaction = fragmentManager.beginTransaction()
//            // For a little polish, specify a transition animation
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            // To make it fullscreen, use the 'content' root view as the container
//            // for the fragment, which is always the root view for the activity
//            transaction
//                .add(android.R.id.content, newFragment)
//                .addToBackStack(null)
//                .commit()
//        }
//    }
}