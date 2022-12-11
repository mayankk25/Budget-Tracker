package com.example.budgettracker

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.view.*
import android.view.View.OnLongClickListener
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.adapters.ViewBindingAdapter.setOnLongClickListener
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.database.Transaction
import kotlinx.coroutines.NonCancellable.cancel


class TransactionAdapter(private val transactionList: ArrayList<Transaction>, private val listener: OnItemClickListener,
                         private val longClickListener: OnLongClickListener, private val context: Context):
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        val titleText: TextView = itemView.findViewById(R.id.list_expense_title)
        val amountText: TextView = itemView.findViewById(R.id.list_expense_amount)
        val descriptionText: TextView = itemView.findViewById(R.id.list_expense_description)
        val iconImage: ImageView = itemView.findViewById(R.id.image_expense_icon)
        val cardViewLayout: CardView = itemView.findViewById(R.id.card_expense_icon)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                longClickListener.onItemLongClick(position, itemView)
            }
            return true
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnLongClickListener {
        fun onItemLongClick(position: Int, itemView: View)
    }

    override fun getItemCount() = transactionList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = transactionList[position]
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