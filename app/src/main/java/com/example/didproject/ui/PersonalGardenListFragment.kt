package com.example.didproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.databinding.FragmentPersonalGardenListBinding
import com.example.didproject.model.adapter.PersonalPlantItemAdapter
import com.example.didproject.viewmodel.PlantCatalogueViewModel
import com.example.didproject.viewmodel.ProfileViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class PersonalGardenListFragment : Fragment() {
    private var _binding: FragmentPersonalGardenListBinding? = null
    private val binding get() = _binding!!
    private lateinit var catalogueViewModel: PlantCatalogueViewModel
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPersonalGardenListBinding.inflate(inflater, container, false)
        catalogueViewModel = ViewModelProvider(requireActivity())[PlantCatalogueViewModel::class.java]
        profileViewModel= ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        val root: View = binding.root

        val gardenFAB : FloatingActionButton = binding.fabAddPlant
        val gardenNoPlantText : TextView = binding.noPlantText
        val gardenList : RecyclerView = binding.recyclerViewGardenList

        profileViewModel.downloadPersonalPlant()

        val navController = findNavController()

        gardenFAB.setOnClickListener {
            navController.popBackStack()
            navController.navigate(R.id.nav_catalogue)
        }

        if(profileViewModel.user.value?.plants?.isEmpty() != false)
            gardenNoPlantText.visibility=View.VISIBLE

        gardenList.layoutManager=LinearLayoutManager(requireContext())
        profileViewModel.personalPlantPhoto.observe(viewLifecycleOwner) {
            gardenList.adapter = PersonalPlantItemAdapter(
                profileViewModel.user.value?.plants!!, it,profileViewModel.user.value?.plants!!.keys.toList(), "")
        }
        return root
    }

}