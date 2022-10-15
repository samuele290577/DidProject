package com.example.didproject.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.databinding.FragmentHomeBinding
import com.example.didproject.model.adapter.HomepageItemAdapter
import com.example.didproject.model.data.User
import com.example.didproject.viewmodel.FriendViewModel
import com.example.didproject.viewmodel.ProfileViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel : ProfileViewModel
    private lateinit var friendViewModel : FriendViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //TODO:problem with adding plant sometimes (list of images)
        //TODO:problem update plant images
        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        friendViewModel = ViewModelProvider(requireActivity())[FriendViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root
        val greetings : TextView = binding.homepageGreeting
        val quote : TextView = binding.homepageQuote
        val gardenButton : Button = binding.hompagePersonalGarden
        val friendButton : Button = binding.homepageFriends
        val gardenList : RecyclerView = binding.recyclerViewHomepagePlantList
        val friendList : RecyclerView = binding.recyclerViewHomepageFriendList
        var user = User()
        val navController = findNavController()


        profileViewModel.user.observe(viewLifecycleOwner){ userValue ->
            user=userValue
            greetings.text = "Ciao, "+user.nickname+"!"
            friendList.adapter = HomepageItemAdapter(userValue.friends.values.map{it.nickname}, userValue.friends.values.map{ Uri.parse(it.imageUri) },userValue.friends.values.map { it.id },false)
            friendList.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        }

        profileViewModel.personalPlantPhoto.observe(viewLifecycleOwner){ map ->
            val user = profileViewModel.user.value!!
            gardenList.adapter = HomepageItemAdapter(user.plants.values.map{it.nickname}, map.values.toList(), user.plants.values.map{it.plantName},true, user.plants.values.map { it.status })
            gardenList.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        }

        //TODO: aggiungere citazioni
        quote.text="Questa è una citazione di prova"

        gardenButton.setOnClickListener {
            navController.popBackStack()
            navController.navigate(R.id.nav_garden)
         }

        friendButton.setOnClickListener {
            navController.popBackStack()
            navController.navigate(R.id.nav_friends)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}