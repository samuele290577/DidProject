package com.example.didproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.databinding.FragmentHomeBinding
import com.example.didproject.model.adapter.HomepageItemAdapter
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

        val navController = findNavController()
        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_home, inclusive = false, saveState = true)
            .setRestoreState(true)
            .build()

        profileViewModel.user.observe(viewLifecycleOwner){
            greetings.text = "Ciao, "+it.nickname+"!"
            if(it.plants.isEmpty())
                gardenButton.text="Aggiungi la tua prima pianta!"
            if(it.friends.isEmpty())
                friendButton.text="Aggiungi il tuo primo vicino!"
        }

        profileViewModel.neighboursPhoto.observe(viewLifecycleOwner){ map ->
            val userValue=profileViewModel.user.value!!
            friendList.adapter = HomepageItemAdapter(map.keys.toList(), map,false, neighbours = userValue.friends)
            friendList.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        }

        profileViewModel.personalPlantPhoto.observe(viewLifecycleOwner){ map ->
            val user = profileViewModel.user.value!!
            gardenList.adapter = HomepageItemAdapter(map.keys.toList(), map,true, plants = user.plants)
            gardenList.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        }

        profileViewModel.trivia.observe(viewLifecycleOwner){
            quote.text=it.random()
        }


        gardenButton.setOnClickListener {
            navController.navigate(R.id.nav_garden, null, navOptions)
         }


        friendButton.setOnClickListener {
            navController.navigate(R.id.nav_friends, null, navOptions)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}