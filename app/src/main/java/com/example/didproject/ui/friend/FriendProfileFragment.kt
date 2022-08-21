package com.example.didproject.ui.friend

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.didproject.R
import com.example.didproject.databinding.FragmentFriendProfileBinding
import com.example.didproject.viewmodel.FriendViewModel
import com.example.didproject.viewmodel.PlantCatalogueViewModel
import com.example.didproject.viewmodel.ProfileViewModel

class FriendProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var friendViewModel: FriendViewModel
    private var _binding: FragmentFriendProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        friendViewModel = ViewModelProvider(requireActivity())[FriendViewModel::class.java]

        val menuHost: MenuHost = requireActivity()

        _binding = FragmentFriendProfileBinding.inflate(inflater, container, false)

        val bio = binding.friendProfileBio
        val name = binding.friendProfileName
        val nickname = binding.friendProfileNickname
        val nOfPlant = binding.friendProfileNOfPlants
        val garden = binding.friendGardenText

        val user = profileViewModel.user.value!!

        val friend = friendViewModel.searchUserById(arguments?.getString("id")!!)

        if(arguments?.getBoolean("new")!!) {
            user.friends.add(friend)
            profileViewModel.updateProfile(user)
        }

        bio.text=friend.bio
        name.text=friend.name
        nickname.text=friend.nickname
        nOfPlant.text=friend.plants.size.toString()
        garden.text="Il giardino di ${friend.nickname}"

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionbar_delete -> {
                        user.friends.remove(friend)
                        profileViewModel.updateProfile(user)
                        val navController = findNavController()
                        navController.navigate(R.id.nav_friends)
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

}