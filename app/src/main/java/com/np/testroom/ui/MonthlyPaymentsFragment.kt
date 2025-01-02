package com.np.testroom.ui
import androidx.lifecycle.Observer
import android.util.Log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.np.testroom.databinding.FragmentMonthlyPaymentsBinding
import com.np.testroom.viewmodels.SharedViewModel
import androidx.lifecycle.ViewModelProvider
import com.np.testroom.adaptors.BalanceAdapter

class MonthlyPaymentsFragment : Fragment() {

    private lateinit var binding: FragmentMonthlyPaymentsBinding
    //private val userViewModel: UserViewModel by viewModels()
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthlyPaymentsBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val recyclerView = binding.recyclerView
        val adapter = BalanceAdapter() // Assuming you have this adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        sharedViewModel.balances.observe(viewLifecycleOwner, Observer { balances ->
            Log.d("Balances", "Updated balances in Fragment 2: $balances")
            if (balances != null) {
                // Assuming you already have a RecyclerView set up
                sharedViewModel.dateString.value?.let { date ->
                    Log.d("Date", "Date associated with balances: $date")
                    adapter.submitList(balances,date) // Update the RecyclerView with the new balances
                }
            }
        })

        return binding.root
    }
}
