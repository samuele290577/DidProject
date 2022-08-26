package com.example.didproject.ui

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.didproject.R
import com.example.didproject.databinding.FragmentCataloguePlantBinding
import com.example.didproject.model.repository.PlantRepository
import com.example.didproject.viewmodel.PlantCatalogueViewModel

class CataloguePlantFragment : Fragment() {

    //TODO: add image

    private var _binding: FragmentCataloguePlantBinding? = null
    private val binding get() = _binding!!
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
        val button : Button = binding.plantButton



        val plant = plantCatalogueViewModel.getByName(arguments?.getString("name")!!)
        name.text=plant.name
        scName.text=plant.scName
        info.text=plant.info
        tips.text=plant.tips
        care.text=plant.care

        button.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("name", plant.name)

            Navigation.findNavController(view = it)
                .navigate(R.id.addPlantToGardenFragment, bundle)
        }

        return root
    }



}