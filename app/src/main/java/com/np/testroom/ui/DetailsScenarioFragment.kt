package com.np.testroom.ui
import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.np.testroom.data.Scenario
import com.np.testroom.databinding.FragmentCalculateLoanBinding
import com.np.testroom.enums.ScenarioCategory
import com.np.testroom.utils.commonFuncs
import com.np.testroom.viewmodels.CalculateLoanViewModel
import com.np.testroom.viewmodels.SharedViewModel
import java.util.Calendar
import com.np.testroom.R
import com.np.testroom.ui.ScenariosFragment

class DetailsScenarioFragment: Fragment() {
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
        val editTextResult=binding.textViewMonthlyPayment
        val categories = ScenarioCategory.values().map { it.name }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter
        editTextName.setText("Test Scenario")
        val buttonCalculer:Button=binding.btnCalculateLoan
        var buttonSauvegarder:Button=binding.btnSauvegarder
        var buttonRetourner:Button=binding.btnRetourner

        buttonCalculer.visibility= View.GONE
        editTextResult.visibility=View.GONE
        editTextStartDate.setOnClickListener {
            showDatePickerDialog()
        }

        val scenario = arguments?.getParcelable<Scenario>("scenario")
        editTextLoan.setText("$" + scenario?.loan.toString())
        editTextName.setText(scenario?.title)
        editTextInterest.setText(scenario?.interestRate.toString())
        editTextPeriod.setText(scenario?.period.toString())
        editTextTerm.setText(scenario?.term.toString())
        editTextExtraPayment.setText("$" +scenario?.extraMonthlyPayment.toString())
        editTextStartDate.setText(commonFuncs.parseMillisToDate(scenario?.startDate,"yyyy-MM-dd"))
        val scenarioCategory = scenario?.category?.name
        val categoryIndex = categories.indexOf(scenarioCategory)
        if (categoryIndex >= 0) {
            categorySpinner.setSelection(categoryIndex)
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

        buttonRetourner.setOnClickListener {
            retourner()
        }

        buttonSauvegarder.setOnClickListener {
            // Get the updated data from the UI
            val updatedTitle = editTextName.text.toString()
            val updatedLoan = editTextLoan.text.toString().removePrefix("$").toDoubleOrNull() ?: 0.0
            val updatedInterestRate = editTextInterest.text.toString().toDoubleOrNull() ?: 0.0
            val updatedPeriod = editTextPeriod.text.toString().toIntOrNull() ?: 0
            val updatedTerm = editTextTerm.text.toString()
            val updatedExtraPayment = editTextExtraPayment.text.toString().removePrefix("$").toDoubleOrNull()
            val updatedStartDate = commonFuncs.parseDateToMillis(editTextStartDate.text.toString(), "yyyy-MM-dd")
            val updatedCategory = categorySpinner.selectedItem?.toString()?.let { ScenarioCategory.valueOf(it) }

            val updatedScenario = scenario?.copy(
                title = updatedTitle,
                loan = updatedLoan,
                interestRate = updatedInterestRate,
                period = updatedPeriod,
                term = updatedTerm,
                extraMonthlyPayment = updatedExtraPayment,
                startDate = updatedStartDate,
                category = updatedCategory
            )
            calculateLoanViewModel.updateScenario(updatedScenario)
            Toast.makeText(requireContext(), "Le scénario fut mis à jour!", Toast.LENGTH_SHORT).show()
            retourner()
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

    private fun retourner(){
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, ScenariosFragment()) // replace with Fragment3
        fragmentTransaction.addToBackStack(null) // Optional: Add to backstack to enable back navigation
        fragmentTransaction.commit()
    }



}