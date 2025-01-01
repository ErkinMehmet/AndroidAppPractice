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

        // Set up DatePickerDialog when the user clicks the EditText
        setTodayDate()
        editTextStartDate.setOnClickListener {
            showDatePickerDialog()
        }


        // Loan EditText (Prefix "$")
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

        // Extra Payment EditText (Prefix "$")
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
                // Handle invalid input (you can show a Toast or error message)
                //binding.resultTextView.text = "Invalid input, please check the fields."
                return@setOnClickListener
            }

            val monthlyRate = interestRate / 100 / 12 // Monthly interest rate
            val yearlyRate=interestRate / 100
            val totalMonths = period*12

            // Calculate the monthly payment using amortization formula
            val monthlyPayment = if (monthlyRate > 0) {
                loanAmount * (monthlyRate * Math.pow(1 + monthlyRate, totalMonths.toDouble())) /
                        (Math.pow(1 + monthlyRate, totalMonths.toDouble()) - 1)
            } else {
                loanAmount / totalMonths
            }

            val monthlyPayment_yearcompounding = if (yearlyRate > 0) {
                loanAmount * (yearlyRate * Math.pow(1 + yearlyRate, period.toDouble())) /
                        (Math.pow(1 + yearlyRate, period.toDouble()) - 1)/12
            } else {
                loanAmount / period
            }

            // List to hold the monthly balances
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

                // Add the monthly balance to the list
                balances.add("$${"%.2f".format(remainingBalance)}")
            }
            monthsTaken=if (monthsTaken>0) {monthsTaken} else {totalMonths}
            val formattedLoanAmount = "$${"%.2f".format(loanAmount)}"
            val formattedInterestRate = "${"%.2f".format(interestRate)}%"
            val formattedPeriod = "$period years"
            val formattedMonthlyPayment = "$${"%.2f".format(monthlyPayment)}"
            val formattedMonthlyPayment_yearcompounding= "$${"%.2f".format(monthlyPayment_yearcompounding)}"
            val formattedExtraPayment = "$${"%.2f".format(extraPayment)}"
            val totalMonthsTaken = commonFuncs.convertMonthsToYearsMonths(monthsTaken)
            // imprimer les informations de base
            binding.textViewMonthlyPayment.text = """
                Loan Amount: $formattedLoanAmount
                Interest Rate: $formattedInterestRate
                Period: $formattedPeriod
                Monthly Payment: $formattedMonthlyPayment
                Yearly Compounding Monthly Payment=$formattedMonthlyPayment_yearcompounding
                Extra Monthly Payment=$formattedExtraPayment
                Total Months: $totalMonthsTaken
            """.trimIndent()

            val newScenario=Scenario(
                title = title,
                loan = loanAmount,
                interestRate = interestRate,
                period = period,
                term = term,
                extraMonthlyPayment = extraPayment,
                userId = sharedPreferences.getInt("userId", 1).toLong(),
                category = category,
                startDate = commonFuncs.parseDateToMillis(dateString)
            )
            Log.d("NewScenario", "New Scenario: $newScenario")
            calculateLoanViewModel.addScenario(newScenario)

            sharedViewModel.setBalances(balances,dateString)
            Toast.makeText(requireContext(), "Balances updated!", Toast.LENGTH_SHORT).show()
        }
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
        val month = calendar.get(Calendar.MONTH) // 0-indexed (January = 0)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Format today's date as yyyy-MM-dd and set it to the EditText
        val formattedDate = commonFuncs.formatDate(year, month, day)
        editTextStartDate.setText(formattedDate)
    }

}
