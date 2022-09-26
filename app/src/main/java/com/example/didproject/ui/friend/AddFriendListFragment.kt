package com.example.didproject.ui.friend

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.adapter.FriendItemAdapter
import com.example.didproject.model.data.User
import com.example.didproject.viewmodel.FriendViewModel
import com.example.didproject.viewmodel.ProfileViewModel

class AddFriendListFragment : Fragment(R.layout.fragment_add_friend_list) {
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var addFriendRecyclerView: RecyclerView

    //TODO: check if already friend + lowercase
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        friendViewModel = ViewModelProvider(requireActivity())[FriendViewModel::class.java]
        addFriendRecyclerView = view.findViewById(R.id.recyclerView_add_friend_list)

        val friendList=profileViewModel.user.value?.friends


        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)

                val sv = menu.findItem(R.id.action_search).actionView as SearchView
                sv.queryHint="Search by name"

                sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String): Boolean {
                        if(newText.isNotEmpty()) {
                            addFriendRecyclerView.adapter = FriendItemAdapter(updateSearchRecycleView(newText,friendList!!),true)
                        }
                        return false
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        addFriendRecyclerView.layoutManager = LinearLayoutManager(this.context)
        val userList = mutableListOf<User>()
        addFriendRecyclerView.adapter = FriendItemAdapter(userList,false)
    }

    private fun updateSearchRecycleView(name:String,friendList:ArrayList<User>): List<User> {
        return friendViewModel.searchUser(name).filter { a-> !friendList.map { it.id }.contains(a.id) }
    }
}