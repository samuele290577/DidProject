package com.example.didproject.ui.friend

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.databinding.FragmentFriendProfileBinding
import com.example.didproject.model.adapter.PersonalPlantItemAdapter
import com.example.didproject.model.adapter.PlantCategoryAdapter
import com.example.didproject.model.data.Plant
import com.example.didproject.model.data.User
import com.example.didproject.model.data.UserPlant
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
        //TODO: photo problem and connection to a garden page???

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

        var friend = User()

        if(arguments?.getBoolean("new")!!) {
            friend=friendViewModel.searchUserById(arguments?.getString("id")!!)
            user.friends.add(friend)
            profileViewModel.updateProfile(user)
        }
        else{
            friend=profileViewModel.findFriendById(arguments?.getString("id")!!)
        }

        bio.text=friend.bio
        name.text=friend.name
        nickname.text=friend.nickname
        nOfPlant.text=friend.plants.size.toString()
        garden.text="Il giardino di ${friend.nickname}"
        Picasso.get().load(Uri.parse(friend.imageUri)).fit().centerCrop().into(image)


        recyclerViewPersonalPlant.layoutManager = LinearLayoutManager(this.context)
        val plantList = mutableListOf<Plant>()
        plantFromUserPlant(plantList,friend.plants)
        recyclerViewPersonalPlant.adapter = PersonalPlantItemAdapter(plantList,friend.plants,arguments?.getString("id")!!)


        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionbar_delete -> {
                        user.friends.remove(friend)
                        profileViewModel.updateProfile(user)
                        MaterialAlertDialogBuilder(requireContext())
                            .setIcon(R.drawable.delete)
                            .setTitle("Elimina")
                            .setMessage("Vuoi eliminare ${friend.nickname} dai tuoi vicini?")
                            .setNeutralButton("Annulla") { _, _ ->
                                // Respond to neutral button press
                                user.friends.add(friend)
                                profileViewModel.updateProfile(user)
                            }
                            .setPositiveButton("Conferma") { _, _ ->

                                val navController = findNavController()
                                navController.navigate(R.id.nav_friends)
                            }
                            .show()


                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    private fun plantFromUserPlant(plantList:MutableList<Plant>,friendPlants:MutableList<UserPlant>){
        val plantlistTmp=plantsViewModel.plantList.value
        friendPlants.forEach{
            plantList.add(
                plantlistTmp?.first{
                        plant:Plant -> it.plantName==plant.name
                }?:Plant())}
    }

}