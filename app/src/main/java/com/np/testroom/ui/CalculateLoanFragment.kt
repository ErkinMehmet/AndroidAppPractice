package com.np.testroom.ui
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.view.MenuItem
import android.widget.Button
import com.np.testroom.R
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.np.testroom.data.Scenario
import com.np.testroom.databinding.FragmentCalculateLoanBinding
import com.np.testroom.enums.ScenarioCategory
import com.np.testroom.utils.commonFuncs
import com.np.testroom.viewmodels.CalculateLoanViewModel
import com.np.testroom.viewmodels.SharedViewModel
import java.util.*
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import android.util.Log
import android.text.Html
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import android.widget.ImageView

class CalculateLoanFragment : Fragment() {

    private lateinit var binding: FragmentCalculateLoanBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var editTextStartDate: EditText
    private lateinit var calculateLoanViewModel: CalculateLoanViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE)
        binding = FragmentCalculateLoanBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        calculateLoanViewModel = ViewModelProvider(this).get(CalculateLoanViewModel::class.java)
        val editTextLoan = binding.editTextLoan
        val editTextExtraPayment = binding.editTextExtraPayment
        val editTextInterest = binding.editTextInterest
        val editTextPeriod = binding.editTextPeriod
        val editTextName = binding.editTextName
        editTextStartDate = binding.editTextStartDate
        val categorySpinner = binding.spinnerCategory
        val editTextTerm=binding.editTextTerm
        val categories = ScenarioCategory.values().map { it.name }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter
        editTextName.setText("Test Scenario")
        var buttonSupprimer:Button=binding.btnSauvegarder
        var buttonRetourner:Button=binding.btnRetourner
        buttonSupprimer.visibility=View.GONE
        buttonRetourner.visibility=View.GONE
        setTodayDate()
        editTextStartDate.setOnClickListener {
            showDatePickerDialog()
        }


        editTextLoan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (!charSequence.isNullOrEmpty() && !charSequence.startsWith("$")) {
                    editTextLoan.setText("$" + charSequence)
                    editTextLoan.setSelection(editTextLoan.text.length) // Move cursor to end
                }
            }
            override fun afterTextChanged(editable: Editable?) {}
        })

        editTextExtraPayment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (!charSequence.isNullOrEmpty() && !charSequence.startsWith("$")) {
                    editTextExtraPayment.setText("$" + charSequence)
                    editTextExtraPayment.setSelection(editTextExtraPayment.text.length)
                }
            }
            override fun afterTextChanged(editable: Editable?) {}
        })

        binding.btnCalculateLoan.setOnClickListener {
            // Get user input
            val loanAmount = editTextLoan.text.toString().replace("$", "").toDoubleOrNull() ?: 0.0
            val interestRate = editTextInterest.text.toString().replace("%", "").toDoubleOrNull() ?: 0.0
            val period = editTextPeriod.text.toString().toIntOrNull() ?: 0
            val extraPayment = editTextExtraPayment.text.toString().replace("$", "").toDoubleOrNull() ?: 0.0
            val dateString = editTextStartDate.text.toString()
            val title=editTextName.text.toString()
            val term = editTextTerm.text.toString().toIntOrNull() ?: 0
            val category = ScenarioCategory.valueOf(categorySpinner.selectedItem.toString())

            if (loanAmount <= 0 || extraPayment < 0 || interestRate <= 0 || period <= 0) {
                return@setOnClickListener
            }

            val monthlyRate = interestRate / 100 / 12 // Monthly interest rate
            val totalMonths = period*12

            val monthlyPayment = if (monthlyRate > 0) {
                loanAmount * (monthlyRate * Math.pow(1 + monthlyRate, totalMonths.toDouble())) /
                        (Math.pow(1 + monthlyRate, totalMonths.toDouble()) - 1)
            } else {
                loanAmount / totalMonths
            }
            val balances = mutableListOf<String>()
            var monthsTaken=0
            var remainingBalance = loanAmount
            for (month in 1..totalMonths) {
                val monthlyInterest = remainingBalance * monthlyRate
                remainingBalance = remainingBalance + monthlyInterest - monthlyPayment - extraPayment


                if (remainingBalance <= 0) {
                    remainingBalance = 0.0
                    if (monthsTaken==0){
                        monthsTaken=month
                    }
                }

                balances.add("$${"%.2f".format(remainingBalance)}")
            }
            monthsTaken=if (monthsTaken>0) {monthsTaken} else {totalMonths}
            val formattedLoanAmount = "$${"%.0f".format(loanAmount)}"
            val formattedInterestRate = "${"%.2f".format(interestRate)}%"
            val formattedMonthlyPayment = "$${"%.0f".format(monthlyPayment+extraPayment)}"
            val startDateMillis=commonFuncs.parseDateToMillis(dateString)
            val dateFin=commonFuncs.parseMillisToDate(commonFuncs.addMonthsToMillis(startDateMillis,monthsTaken),"yyyy-MM-dd")
            binding.textViewMonthlyPayment.text = Html.fromHtml("""
                <b>Montant initial:</b> $formattedLoanAmount<br>
                <b>Taux d'intérêt:</b> $formattedInterestRate<br>
                <b>Paiement mensuel:</b> $formattedMonthlyPayment<br>
                <b>Jusqu'au:</b> $dateFin
            """.trimIndent())
            Log.d("Test",sharedPreferences.getInt("userId", 0).toString())
            val newScenario=Scenario(
                title = title,
                loan = loanAmount,
                interestRate = interestRate,
                period = period,
                term = term.toString(),
                extraMonthlyPayment = extraPayment,
                userId = sharedPreferences.getInt("userId", 1).toLong(),
                category = category,
                startDate = startDateMillis
            )
            Log.d("NewScenario", "Nouveau scénario: $newScenario")
            calculateLoanViewModel.addScenario(newScenario)

            sharedViewModel.setBalances(balances,dateString)
            Toast.makeText(requireContext(), "Les paiements mis à jour!", Toast.LENGTH_SHORT).show()



        }
        /*
        val bottomNavigationView: BottomNavigationView? = activity?.findViewById(R.id.bottom_navigation)
        val menuItem: MenuItem? = bottomNavigationView?.menu?.findItem(R.id.nav_add)
        val menuItemIcon: ImageView? = menuItem?.actionView?.findViewById(android.R.id.icon)
        val glowAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.glow)
        menuItemIcon?.startAnimation(glowAnimation)*/
        return binding.root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = commonFuncs.formatDate(selectedYear, selectedMonth, selectedDay)
                editTextStartDate.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
    private fun setTodayDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val formattedDate = commonFuncs.formatDate(year, month, day)
        editTextStartDate.setText(formattedDate)
    }

}
