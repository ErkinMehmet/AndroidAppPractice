package com.np.testroom.ui
import androidx.lifecycle.Observer
import android.util.Log

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
import com.np.testroom.viewmodel.SharedViewModel
import androidx.lifecycle.ViewModelProvider

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
        val adapter = LoanBalanceAdapter() // Assuming you have this adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        sharedViewModel.balances.observe(viewLifecycleOwner, Observer { balances ->
            Log.d("Balances", "Updated balances in Fragment 2: $balances")
            if (balances != null) {
                // Assuming you already have a RecyclerView set up
                adapter.submitList(balances) // Update the RecyclerView with the new balances
            }
        })

        return binding.root
    }
}
