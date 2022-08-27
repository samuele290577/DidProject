package com.example.didproject.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.databinding.FragmentHomeBinding
import com.example.didproject.model.adapter.HomepageItemAdapter
import com.example.didproject.model.adapter.PlantCategoryAdapter
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
            gardenList.adapter = HomepageItemAdapter(userValue.plants.map{it.nickname}, userValue.plants.map{ Uri.parse("")}, userValue.plants.map{it.plantName},true)
            friendList.adapter = HomepageItemAdapter(userValue.friends.map{it.nickname}, userValue.friends.map{ Uri.parse(it.imageUri)},user.friends.map { it.id },false)
        }

        //TODO: aggiungere citazioni
        quote.text="Questa è una citazione di prova"

        gardenButton.setOnClickListener {
            navController.navigate(R.id.nav_garden)
         }

        friendButton.setOnClickListener {
            navController.navigate(R.id.nav_friends)
        }

        gardenList.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        friendList.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}