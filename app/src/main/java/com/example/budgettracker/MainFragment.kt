package com.example.budgettracker

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Adapter
import android.widget.AdapterView
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
import io.grpc.InternalChannelz.id
import java.math.BigDecimal

class MainFragment: Fragment(), TransactionAdapter.OnItemClickListener, TransactionAdapter.OnLongClickListener {
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

        adapter = context?.let { TransactionAdapter(transactionList, this, this, it) }!!
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
                    binding.totalExpenseAmount.text = "$" + viewModel.expenseTotal.toString()
                    binding.totalIncomeAmount.text = "$" + viewModel.incomeTotal.toString()
                    binding.totalBalanceAmount.text = "$" + (viewModel.incomeTotal - viewModel.expenseTotal).toString()
                    binding.budgetList.adapter = adapter
                }
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "ERROR", e)
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }

        binding.button.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToLoginFragment())
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        //Toast.makeText(context, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = transactionList[position]
        //changes the title for the clicked item temporarily. If the main fragment page is shown again, it will be removed
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToEditTransactionDialogFragment2(clickedItem))
        adapter?.notifyItemChanged(position)
    }

    override fun onItemLongClick(position: Int, itemView: View) {
        Toast.makeText(context, "Item $position long clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = transactionList[position]
        adapter?.notifyItemChanged(position)
    }
}