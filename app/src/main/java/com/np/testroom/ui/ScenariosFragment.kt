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
import com.np.testroom.adaptors.ScenarioAdapter

class ScenariosFragment : Fragment() {

    private lateinit var viewModel: CalculateLoanViewModel
    private lateinit var scenarioAdapter: ScenarioAdapter
    private var _binding: FragmentScenariosBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

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

        viewModel = ViewModelProvider(this).get(CalculateLoanViewModel::class.java)

        val userId = sharedPreferences.getInt("userId", -1)

        viewModel.getScenariosByUserId(userId).observe(viewLifecycleOwner, Observer { scenarios ->
            scenarioAdapter = ScenarioAdapter(scenarios) { scenario ->
                // Handle item click here (e.g., navigate to details fragment)
                navigateToScenarioDetail(scenario)
            }

            // Set up RecyclerView with the adapter
            binding.recyclerViewScenarios.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewScenarios.adapter = scenarioAdapter
        })
    }

    private fun navigateToScenarioDetail(scenario: Scenario) {
        // Implement navigation to another fragment with the scenario's details
        Log.d("TAG", "This is a debug log message")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding to avoid memory leaks
    }
}