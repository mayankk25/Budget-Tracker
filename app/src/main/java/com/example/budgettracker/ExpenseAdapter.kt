package com.example.budgettracker

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.database.Expense
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.concurrent.RecursiveAction

class ExpenseAdapter(private val expenseList: ArrayList<Expense>): RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.textView)
        val amountText: TextView = itemView.findViewById(R.id.textView2)
        val descriptionText: TextView = itemView.findViewById(R.id.textView3)
    }

    override fun getItemCount() = expenseList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = expenseList[position]
        //holder.textView.text = item.title.toString()
        holder.titleText.text = item.title
        holder.amountText.text = item.amount.toString()
        holder.descriptionText.text = item.description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_item_view, parent, false)
        return ViewHolder(view)
    }
}