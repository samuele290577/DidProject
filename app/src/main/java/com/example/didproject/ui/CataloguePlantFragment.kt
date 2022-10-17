package com.example.didproject.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.didproject.R
import com.example.didproject.databinding.FragmentCataloguePlantBinding
import com.example.didproject.viewmodel.PlantCatalogueViewModel
import com.squareup.picasso.Picasso

class CataloguePlantFragment : Fragment() {

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
        val button : Button = binding.plantButton
        val image : ImageView = binding.plantCatalogueImage
        val humidity : TextView = binding.plantCatalogueHumidity
        val sun : TextView = binding.plantCatalogueSun
        val difficulty : TextView = binding.plantCatalogueDifficulty
        val scName : TextView = binding.plantScnameCatalogue

        val labelInfo : TextView = binding.labelInfo
        val labelCura : TextView = binding.labelCura
        val labelTips : TextView = binding.labelConsigli
        val tipText : TextView = binding.tipsPlaceholder


        val plant = plantCatalogueViewModel.getByName(arguments?.getString("name")!!)
        Log.d("plant",plant.toString())

        tipText.text = plant.info
        name.text=plant.name
        scName.text=plant.scName
        humidity.text = plant.humidity.toString()
        difficulty.text = plant.difficulty.toString()
        sun.text = plant.sun.toString()
        plantCatalogueViewModel.photoList.observe(viewLifecycleOwner){
            if(it.containsKey(plant.name))
                Picasso.get().load(it[plant.name]).fit().centerCrop().into(image)
        }


        labelInfo.setOnClickListener {
            Log.d("clicked", "clicked")
            tipText.text = plant.info
            labelCura.setTextColor(Color.WHITE)
            labelInfo.setTextColor(Color.GREEN)
            labelTips.setTextColor(Color.WHITE)
        }
        labelCura.setOnClickListener {
            Log.d("clicked", "clicked")
            tipText.text = plant.care
           labelCura.setTextColor(Color.GREEN)
            labelInfo.setTextColor(Color.WHITE)
            labelTips.setTextColor(Color.WHITE)

        }
        labelTips.setOnClickListener {
            Log.d("clicked", "clicked")
            tipText.text = plant.tips
            labelCura.setTextColor(Color.WHITE)
            labelInfo.setTextColor(Color.WHITE)
            labelTips.setTextColor(Color.GREEN)
        }

        button.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("name", plant.name)

            Navigation.findNavController(view = it)
                .navigate(R.id.addPlantToGardenFragment, bundle)
        }

        return root
    }

        //val info : TextView = binding.plantInfoCatalogue
        //val care : TextView = binding.plantCareCatalogue
        //val tips : TextView = binding.plantTipCatalogue




}