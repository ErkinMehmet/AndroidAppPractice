package com.np.testroom.adaptors
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.np.testroom.R
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*
import com.np.testroom.utils.commonFuncs

class LoanBalanceAdapter : RecyclerView.Adapter<LoanBalanceAdapter.LoanBalanceViewHolder>() {

    private var balances: List<String> = listOf()
    private var dateString:String=""

    // View Holder to represent each row in the RecyclerView
    inner class LoanBalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val balanceTextView: TextView = itemView.findViewById(R.id.textViewBalance)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
    }

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanBalanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loan_balance, parent, false)
        return LoanBalanceViewHolder(view)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: LoanBalanceViewHolder, position: Int) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.odd_row_background); // Use odd row color
        } else {
            holder.itemView.setBackgroundResource(R.drawable.even_row_background); // Use even row color
        }

        holder.balanceTextView.text = balances[position]
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate: Date? = try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
        if (parsedDate != null) {
            val calendar = Calendar.getInstance()
            calendar.time = parsedDate
            calendar.add(Calendar.MONTH, position + 1)
            val updatedDateString = dateFormat.format(calendar.time)
            holder.dateTextView.text = updatedDateString
        } else {
            holder.dateTextView.text = "In "+commonFuncs.convertMonthsToYearsMonths(position +1)
        }
    }

    // Return the size of the list
    override fun getItemCount(): Int {
        return balances.size
    }

    // Submit a new list of balances
    fun submitList(newBalances: List<String>,date:String) {
        balances = newBalances
        dateString=date
        notifyDataSetChanged()
    }
}
