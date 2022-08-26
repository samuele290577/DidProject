package com.example.didproject.ui.friend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.adapter.FriendItemAdapter
import com.example.didproject.model.data.User
import com.example.didproject.viewmodel.FriendViewModel
import com.example.didproject.viewmodel.ProfileViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlreadyFriendListFragment : Fragment(R.layout.fragment_already_friend_list) {
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var alreadyFriendRecyclerView: RecyclerView

    //TODO:correggere problema foto

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_friend)

        friendViewModel = ViewModelProvider(requireActivity())[FriendViewModel::class.java]
        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        alreadyFriendRecyclerView = view.findViewById(R.id.recyclerView_friend_list)

        val navController = findNavController()

        fab.setOnClickListener {
            navController.navigate(R.id.addFriendListFragment)
        }

        alreadyFriendRecyclerView.layoutManager = LinearLayoutManager(this.context)
        val userList = mutableListOf<User>()

        initialize(userList)
        alreadyFriendRecyclerView.adapter = FriendItemAdapter(userList,false)
    }


    private fun initialize(list:MutableList<User>) {
        profileViewModel.user.value?.friends?.forEach { list.add(it) }
    }

}