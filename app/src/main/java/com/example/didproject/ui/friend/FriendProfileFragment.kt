package com.example.didproject.ui.friend

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.databinding.FragmentFriendProfileBinding
import com.example.didproject.model.adapter.PersonalPlantItemAdapter
import com.example.didproject.viewmodel.FriendViewModel
import com.example.didproject.viewmodel.PlantCatalogueViewModel
import com.example.didproject.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

class FriendProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var plantsViewModel: PlantCatalogueViewModel
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var recyclerViewPersonalPlant: RecyclerView
    private var _binding: FragmentFriendProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        friendViewModel = ViewModelProvider(requireActivity())[FriendViewModel::class.java]
        plantsViewModel = ViewModelProvider(requireActivity())[PlantCatalogueViewModel::class.java]

        val menuHost: MenuHost = requireActivity()

        _binding = FragmentFriendProfileBinding.inflate(inflater, container, false)

        recyclerViewPersonalPlant = binding.recyclerViewFriendPlants

        val bio = binding.friendProfileBio
        val name = binding.friendProfileName
        val nickname = binding.friendProfileNickname
        val nOfPlant = binding.friendProfileNOfPlants
        val garden = binding.friendGardenText
        val image = binding.friendProfileImage

        val user = profileViewModel.user.value!!


        var friend = friendViewModel.searchUserById(arguments?.getString("id")!!).toNeighbour()
        friendViewModel.users.observe(viewLifecycleOwner) {
            friend = friendViewModel.searchUserById(friend.id).toNeighbour()
            user.friends[friend.id] = friend
            profileViewModel.updateProfile(user, 2)
            profileViewModel.downloadPersonalPlantNeighbour(friend.id)
        }

        profileViewModel.user.observe(viewLifecycleOwner) {
            bio.text = it.friends[friend.id]?.bio
            name.text = it.friends[friend.id]?.name
            nickname.text = it.friends[friend.id]?.nickname
            nOfPlant.text = it.friends[friend.id]?.plants?.size.toString()
            garden.text = "Il giardino di ${it.friends[friend.id]?.nickname}"
        }

        profileViewModel.neighboursPhoto.observe(viewLifecycleOwner){
            if(it.containsKey(friend.id))
            Picasso.get().load(
                it[friend.id])
                .fit().centerCrop().into(image)
        }

        recyclerViewPersonalPlant.layoutManager = LinearLayoutManager(this.context)

        profileViewModel.personalNeighbourPlantPhoto.observe(viewLifecycleOwner) {
            recyclerViewPersonalPlant.adapter =
                PersonalPlantItemAdapter(friend.plants, it, friend.plants.keys.toList(),arguments?.getString("id")!!)
        }

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionbar_delete -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setIcon(R.drawable.delete)
                            .setTitle("Elimina")
                            .setMessage("Vuoi eliminare ${friend.nickname} dai tuoi vicini?")
                            .setNeutralButton("Annulla") { _, _ ->
                                // Respond to neutral button press
                            }
                            .setPositiveButton("Conferma") { _, _ ->
                                profileViewModel.removeFriend(friend.id)
                                val navController = findNavController()
                                navController.popBackStack()
                                navController.navigate(R.id.nav_home)
                            }
                            .show()


                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        val activity=requireActivity() as AppCompatActivity
        activity.supportActionBar?.title="Profilo di "+ friend.nickname
        return binding.root
    }

}