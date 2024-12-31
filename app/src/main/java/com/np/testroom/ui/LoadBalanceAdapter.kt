package com.np.testroom.ui
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.np.testroom.R
import android.widget.TextView



class LoanBalanceAdapter : RecyclerView.Adapter<LoanBalanceAdapter.LoanBalanceViewHolder>() {

    private var balances: List<String> = listOf()

    // View Holder to represent each row in the RecyclerView
    inner class LoanBalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val balanceTextView: TextView = itemView.findViewById(R.id.textViewBalance)
    }

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanBalanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loan_balance, parent, false)
        return LoanBalanceViewHolder(view)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: LoanBalanceViewHolder, position: Int) {
        holder.balanceTextView.text = balances[position]
    }

    // Return the size of the list
    override fun getItemCount(): Int {
        return balances.size
    }

    // Submit a new list of balances
    fun submitList(newBalances: List<String>) {
        balances = newBalances
        notifyDataSetChanged()
    }
}
