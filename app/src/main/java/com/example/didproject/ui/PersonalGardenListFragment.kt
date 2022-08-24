package com.example.didproject.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.databinding.FragmentPersonalGardenListBinding
import com.example.didproject.databinding.FragmentPersonalPlantBinding
import com.example.didproject.model.adapter.PersonalPlantItemAdapter
import com.example.didproject.model.data.Plant
import com.example.didproject.model.data.UserPlant
import com.example.didproject.viewmodel.PlantCatalogueViewModel
import com.example.didproject.viewmodel.ProfileViewModel


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

        val gardenList : RecyclerView = binding.recyclerViewGardenList
        val plantList = mutableListOf<Plant>()

        plantFromUserPlant(plantList,profileViewModel.user.value?.plants!!)
        gardenList.layoutManager=LinearLayoutManager(requireContext())
        gardenList.adapter = PersonalPlantItemAdapter(plantList,profileViewModel.user.value?.plants?:listOf(UserPlant()),"")
        return root
    }

    private fun plantFromUserPlant(plantList:MutableList<Plant>, friendPlants:MutableList<UserPlant>){
        val plantlistTmp=catalogueViewModel.plantList.value
        friendPlants.forEach{
            plantList.add(
                plantlistTmp?.first{
                        plant: Plant -> it.plantName==plant.name
                }?: Plant()
            )}
    }

}