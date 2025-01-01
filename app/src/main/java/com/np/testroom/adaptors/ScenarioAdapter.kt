package com.np.testroom.adaptors

import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.np.testroom.R
import android.widget.TextView
import com.np.testroom.data.Scenario
import java.text.SimpleDateFormat
import java.util.*

class ScenarioAdapter(private var scenarios: List<Scenario>, private val onClick: (Scenario) -> Unit) :
    RecyclerView.Adapter<ScenarioAdapter.ScenarioViewHolder>() {

    // View Holder to represent each row in the RecyclerView
    inner class ScenarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val loanTextView: TextView = itemView.findViewById(R.id.textViewLoan)
        val interestRateTextView: TextView = itemView.findViewById(R.id.textViewInterestRate)
        val startDateTextView: TextView = itemView.findViewById(R.id.textViewStartDate)

        init {
            itemView.setOnClickListener {
                onClick(scenarios[adapterPosition])  // Trigger click listener with the scenario at the current position
            }
        }
    }

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScenarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_scenario, parent, false)
        return ScenarioViewHolder(view)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: ScenarioViewHolder, position: Int) {
        val scenario = scenarios[position]

        // Bind data to the views
        holder.titleTextView.text = scenario.title
        holder.loanTextView.text = scenario.loan.toString()
        holder.interestRateTextView.text = scenario.interestRate.toString()

        // Format the start date
        val startDate = Date(scenario.startDate)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.startDateTextView.text = format.format(startDate)
    }

    // Return the size of the list
    override fun getItemCount(): Int {
        return scenarios.size
    }

    // Submit a new list of scenarios
    fun submitList(newScenarios: List<Scenario>) {
        scenarios = newScenarios
        notifyDataSetChanged()
    }
}
