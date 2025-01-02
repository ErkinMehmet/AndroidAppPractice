package com.np.testroom.adaptors
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.np.testroom.R
import android.widget.TextView
import android.widget.Button
import com.np.testroom.data.Scenario
import com.np.testroom.utils.commonFuncs
import androidx.core.text.HtmlCompat


class ScenarioAdapter(private var scenarios: List<Scenario>, private val onClick: (Scenario) -> Unit,
                      private val onDelete: (Scenario) -> Unit,
                      private val onModify: (Scenario) -> Unit) :
    RecyclerView.Adapter<ScenarioAdapter.ScenarioViewHolder>() {

    // View Holder to represent each row in the RecyclerView
    inner class ScenarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val loanTextView: TextView = itemView.findViewById(R.id.textViewLoan)
        val conditionsTextView: TextView = itemView.findViewById(R.id.textViewConditions)
        val actualPaymentsTextView: TextView = itemView.findViewById(R.id.textViewActualPayments)
        val supprimerButton: Button=itemView.findViewById(R.id.buttonSupprimer)
        val modifierButton: Button=itemView.findViewById(R.id.buttonModifier)

        init {
            itemView.setOnClickListener {
                onClick(scenarios[adapterPosition])  // Trigger click listener with the scenario at the current position
            }
            // Set up the delete button click listener
            supprimerButton.setOnClickListener {
                onDelete(scenarios[adapterPosition]) // Call onDelete when button is clicked
            }

            modifierButton.setOnClickListener {
                onModify(scenarios[adapterPosition])
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
        val loanAmount=scenario.loan
        val interest=scenario.interestRate
        val period=scenario.period
        val totalMonths=period*12
        val extra= if (scenario.extraMonthlyPayment !=null) {scenario.extraMonthlyPayment} else {0.0}
        val monthlyRate=interest/12
        val monthlyPayment = if (monthlyRate > 0) {
            loanAmount * (monthlyRate * Math.pow(1 + monthlyRate, totalMonths.toDouble())) /
                    (Math.pow(1 + monthlyRate, totalMonths.toDouble()) - 1)
        } else {
            loanAmount / totalMonths
        }
        var monthsTaken=0
        var remainingBalance = loanAmount
        for (month in 1..totalMonths) {
            val monthlyInterest = remainingBalance * monthlyRate
            remainingBalance = remainingBalance + monthlyInterest - monthlyPayment - extra
            if (remainingBalance <= 0) {
                remainingBalance = 0.0
                if (monthsTaken==0){
                    monthsTaken=month
                }
            }
        }
        monthsTaken=if (monthsTaken>0) {monthsTaken} else {totalMonths}
        val dateCreation=commonFuncs.parseMillisToDate(scenario.createdAt,"yyyy-MM-dd")
        val dateFin=commonFuncs.parseMillisToDate(commonFuncs.addMonthsToMillis(scenario.startDate,monthsTaken),"yyyy-MM-dd")
        holder.titleTextView.text = "${scenario.title} créé le ${dateCreation}"
        holder.loanTextView.text = HtmlCompat.fromHtml("<b>Montant initial:</b> $loanAmount", HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.conditionsTextView.text = HtmlCompat.fromHtml("<b>Taux d'intérêt:</b> $interest%<br><b>Pour:</b> ${commonFuncs.convertMonthsToYearsMonths(period)}", HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.actualPaymentsTextView.text = HtmlCompat.fromHtml("<b>Paiement mensuel:</b> ${monthlyPayment + extra}%<br><b>Jusqu'au:</b> $dateFin", HtmlCompat.FROM_HTML_MODE_LEGACY)

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
