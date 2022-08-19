package com.example.didproject.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.adapter.FriendItemAdapter
import com.example.didproject.model.adapter.PlantItemAdapter
import com.example.didproject.model.data.Plant
import com.example.didproject.model.data.User
import com.example.didproject.viewmodel.FriendViewModel
import com.example.didproject.viewmodel.PlantCatalogueViewModel
import com.example.didproject.viewmodel.ProfileViewModel

class FriendListFragment : Fragment(R.layout.fragment_friend_list) {
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendViewModel = ViewModelProvider(requireActivity())[FriendViewModel::class.java]
        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        recyclerView = view.findViewById(R.id.recyclerView_friend_list)

        // configuring recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val userList = mutableListOf<User>()
        initialize(userList)
        recyclerView.adapter = FriendItemAdapter(userList)

    }


    private fun initialize(list:MutableList<User>) {
        profileViewModel.user.value?.friends?.forEach { list.add(it) }
    }
}