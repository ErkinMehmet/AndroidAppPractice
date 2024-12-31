package com.np.testroom.ui

import androidx.lifecycle.ViewModel
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.np.testroom.databinding.FragmentAddUserBinding
import com.np.testroom.viewmodel.UserViewModel
import com.np.testroom.data.User
import com.np.testroom.databinding.FragmentCalculateLoanBinding
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.np.testroom.databinding.FragmentMonthlyPaymentsBinding
import com.np.testroom.R
import com.np.testroom.viewmodel.SharedViewModel
import androidx.lifecycle.ViewModelProvider
import android.util.Log
import com.np.testroom.viewmodel.CalculateLoanViewModel

class CalculateLoanFragment : Fragment() {

    private lateinit var binding: FragmentCalculateLoanBinding
    private lateinit var sharedViewModel: SharedViewModel
    private val calculateLoanViewModel: CalculateLoanViewModel by viewModels()
    //private val userViewModel: UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculateLoanBinding.inflate(inflater, container, false)
        val editTextLoan = binding.editTextLoan
        val editTextExtraPayment = binding.editTextExtraPayment
        val editTextInterest = binding.editTextInterest
        val editTextPeriod = binding.editTextPeriod
        val editTextName = binding.editTextName


        editTextName.setText("Test")

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


        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        binding.btnCalculateLoan.setOnClickListener {
            // Get user input
            val loanAmount = editTextLoan.text.toString().replace("$", "").toDoubleOrNull() ?: 0.0
            val interestRate = editTextInterest.text.toString().replace("%", "").toDoubleOrNull() ?: 0.0
            val period = editTextPeriod.text.toString().toIntOrNull() ?: 0
            val extraPayment = editTextExtraPayment.text.toString().replace("$", "").toDoubleOrNull() ?: 0.0


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


                if (remainingBalance < 0) {
                    remainingBalance = 0.0
                    if (monthsTaken==0){
                        monthsTaken=month
                    }
                }

                // Add the monthly balance to the list
                balances.add("Month $month: $${"%.2f".format(remainingBalance)}")
            }

            val formattedLoanAmount = "$${"%.2f".format(loanAmount)}"
            val formattedInterestRate = "${"%.2f".format(interestRate)}%"
            val formattedPeriod = "$period years"
            val formattedMonthlyPayment = "$${"%.2f".format(monthlyPayment)}"
            val formattedMonthlyPayment_yearcompounding= "$${"%.2f".format(monthlyPayment_yearcompounding)}"
            val formattedExtraPayment = "$${"%.2f".format(extraPayment)}"
            val totalMonthsTaken = monthsTaken
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

            /*
            val recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = LoanBalanceAdapter()
            recyclerView.adapter = adapter
            adapter.submitList(balances)
            Log.d("Balances", "Balances in Fragment 1: $balances")
            */
            sharedViewModel.setBalances(balances)
            calculateLoanViewModel.test()
        }
        return binding.root
    }
}
