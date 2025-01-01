package com.np.testroom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.np.testroom.adaptors.UserAdapter
import com.np.testroom.databinding.FragmentUserListBinding
import com.np.testroom.viewmodels.UserViewModel

class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(inflater, container, false)

        userAdapter = UserAdapter(3)
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUsers.adapter = userAdapter

        userViewModel.usersLiveData.observe(viewLifecycleOwner, { users ->
            userAdapter.updateList(users)
        })

        userViewModel.getAllUsers()

        return binding.root
    }
}
