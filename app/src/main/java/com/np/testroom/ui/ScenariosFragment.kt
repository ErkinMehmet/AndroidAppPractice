package com.np.testroom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.np.testroom.data.Scenario
import com.np.testroom.databinding.FragmentScenariosBinding
import com.np.testroom.viewmodels.CalculateLoanViewModel
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import com.np.testroom.R
import com.np.testroom.adaptors.ScenarioAdapter
import com.np.testroom.ui.MonthlyPaymentsFragment
import com.np.testroom.ui.DetailsScenarioFragment
import com.np.testroom.utils.commonFuncs
import com.np.testroom.viewmodels.SharedViewModel

class ScenariosFragment : Fragment() {

    private lateinit var viewModel: CalculateLoanViewModel
    private lateinit var scenarioAdapter: ScenarioAdapter
    private var _binding: FragmentScenariosBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScenariosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel = ViewModelProvider(this).get(CalculateLoanViewModel::class.java)

        val userId = sharedPreferences.getInt("userId", 1)

        viewModel.getScenariosByUserId(userId).observe(viewLifecycleOwner, Observer { scenarios ->
            scenarioAdapter = ScenarioAdapter(scenarios,onClick= { scenario ->
                navigateToScenarioDetail(scenario)
            }, onDelete={ scenario ->
                viewModel.deleteScenario(scenario.id)
                val updatedScenarios = scenarios.toMutableList()
                updatedScenarios.remove(scenario)
                scenarioAdapter.submitList(updatedScenarios)
            }, onModify={
                scenario ->
                val bundle = Bundle().apply {
                    putParcelable("scenario", scenario)  // Assuming Scenario is Parcelable
                }
                val fragment =DetailsScenarioFragment()
                fragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)  // Replace with your container's ID
                    .addToBackStack(null)
                    .commit()
                }
            )


            // Set up RecyclerView with the adapter
            binding.recyclerViewScenarios.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewScenarios.adapter = scenarioAdapter
        })
    }

    private fun navigateToScenarioDetail(scenario: Scenario) {
        // Implement navigation to another fragment with the scenario's details
        val frag=MonthlyPaymentsFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(R.id.fragment_container,frag)
        transaction.addToBackStack(null)
        transaction.commit()

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
        val balances = mutableListOf<String>()
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
            balances.add("$${"%.2f".format(remainingBalance)}")
        }
        monthsTaken=if (monthsTaken>0) {monthsTaken} else {totalMonths}
        val dateFin= commonFuncs.parseMillisToDate(commonFuncs.addMonthsToMillis(scenario.startDate,monthsTaken))
        sharedViewModel.setBalances(balances,dateFin)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding to avoid memory leaks
    }
}