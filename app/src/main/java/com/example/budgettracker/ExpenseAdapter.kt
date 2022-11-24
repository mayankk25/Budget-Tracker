package com.example.budgettracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.database.Transaction


class ExpenseAdapter(private val expenseList: ArrayList<Transaction>, private val context: Context):
    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.list_expense_title)
        val amountText: TextView = itemView.findViewById(R.id.list_expense_amount)
        val descriptionText: TextView = itemView.findViewById(R.id.list_expense_description)
        val iconImage: ImageView = itemView.findViewById(R.id.image_expense_icon)
        val cardViewLayout: CardView = itemView.findViewById(R.id.card_expense_icon)
    }

    override fun getItemCount() = expenseList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = expenseList[position]
        //holder.textView.text = item.title.toString()
        holder.titleText.text = item.title
        holder.amountText.text = item.amount.toString()
        holder.descriptionText.text = item.date
        holder.iconImage.setImageResource (
            when (item.category) {
                "Food" -> R.drawable.ic_round_local_dining_24
                "Transportation" -> R.drawable.ic_round_directions_transportation_24
                "Entertainment" -> R.drawable.ic_baseline_entertainment_24
                "Education" -> R.drawable.ic_round_school_24
                "Health" -> R.drawable.ic_round_health_and_safety_24
                "Self-Development" -> R.drawable.ic_round_self_development_24
                else -> R.drawable.ic_round_add_circle_24
            }
        )
        holder.iconImage.setColorFilter(
            when (item.category) {
                "Food" -> getColor(context, R.color.dark_blue)
                "Transportation" -> getColor(context, R.color.yellow)
                "Entertainment" -> getColor(context, R.color.orange)
                "Education" -> getColor(context, R.color.green)
                "Health" -> getColor(context, R.color.red)
                "Self-Development" -> getColor(context, R.color.purple)
                else -> getColor(context, R.color.teal_700)
            }
        )
        holder.cardViewLayout.setCardBackgroundColor(
            when (item.category) {
                "Food" -> getColor(context, R.color.light_blue)
                "Transportation" -> getColor(context, R.color.light_yellow)
                "Entertainment" -> getColor(context, R.color.light_orange)
                "Education" -> getColor(context, R.color.light_green)
                "Health" -> getColor(context, R.color.light_red)
                "Self-Development" -> getColor(context, R.color.light_purple)
                else -> getColor(context, R.color.light_teal)
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_item_view, parent, false)
        return ViewHolder(view)
    }
}