package com.example.didproject

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.didproject.databinding.FragmentCataloguePlantBinding
import com.example.didproject.model.repository.PlantRepository
import com.example.didproject.viewmodel.PlantCatalogueViewModel
import com.example.didproject.viewmodel.ProfileViewModel

class CataloguePlantFragment : Fragment() {


    private var _binding: FragmentCataloguePlantBinding? = null
    private val binding get() = _binding!!
    private val plantRepo = PlantRepository()
    private lateinit var plantCatalogueViewModel: PlantCatalogueViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        plantCatalogueViewModel = ViewModelProvider(requireActivity())[PlantCatalogueViewModel::class.java]
        _binding = FragmentCataloguePlantBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val name : TextView = binding.plantNameCatalogue
        val scName : TextView = binding.plantScnameCatalogue
        val info : TextView = binding.plantInfoCatalogue
        val care : TextView = binding.plantCareCatalogue
        val tips : TextView = binding.plantTipCatalogue



        val plant = plantCatalogueViewModel.getCategory(arguments?.getString("category")!!)[0]
        name.text=plant.name
        scName.text=plant.scName
        info.text=plant.info
        tips.text=plant.tips
        care.text=plant.care

        return root
    }



}