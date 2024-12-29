package com.np.testroom.ui

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

class AddUserFragment : Fragment() {

    private lateinit var binding: FragmentAddUserBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddUserBinding.inflate(inflater, container, false)

        binding.btnAddUser.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val age = binding.editTextAge.text.toString().toIntOrNull()

            if (!TextUtils.isEmpty(name) && age != null) {
                val user = User(name = name, age = age)
                userViewModel.addUser(user)
                binding.editTextName.text.clear()
                binding.editTextAge.text.clear()
            }
        }

        return binding.root
    }
}
