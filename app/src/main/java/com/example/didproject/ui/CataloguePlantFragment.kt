package com.example.didproject.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
        val humidity : ImageView = binding.plantCatalogueHumidity
        val sun : ImageView = binding.plantCatalogueSun
        val difficulty : ImageView = binding.plantCatalogueDifficulty
        val scName : TextView = binding.plantScnameCatalogue

        val labelInfo : TextView = binding.labelInfo
        val labelCura : TextView = binding.labelCura
        val labelTips : TextView = binding.labelConsigli
        val tipText : TextView = binding.tipsPlaceholder

        tipText.setMovementMethod(ScrollingMovementMethod())


        val plant = plantCatalogueViewModel.getByName(arguments?.getString("name")!!)
        Log.d("plant",plant.toString())

        tipText.text = plant.info
        name.text=plant.name
        scName.text=plant.scName

        val levelOne : Drawable = resources.getDrawable(R.drawable.level_one)
        val levelTwo : Drawable = resources.getDrawable(R.drawable.level_two)
        val levelThree : Drawable = resources.getDrawable(R.drawable.level_three)

        val activity=requireActivity() as AppCompatActivity
        activity.supportActionBar?.title="Pagina "+plant.name

        when (plant.humidity){
            1 -> humidity.setImageDrawable(levelOne)
            2 -> humidity.setImageDrawable(levelTwo)
            else -> {
                humidity.setImageDrawable(levelThree)
            }
        }
        when (plant.difficulty){
            1 -> difficulty.setImageDrawable(levelOne)
            2 -> difficulty.setImageDrawable(levelTwo)
            else -> {
                difficulty.setImageDrawable(levelThree)
            }
        }
        when (plant.sun){
            1 -> sun.setImageDrawable(levelOne)
            2 -> sun.setImageDrawable(levelTwo)
            else -> {
                sun.setImageDrawable(levelThree)
            }
        }




        plantCatalogueViewModel.photoList.observe(viewLifecycleOwner){
            if(it.containsKey(plant.name))
                Picasso.get().load(it[plant.name]).fit().centerCrop().into(image)
        }


        labelInfo.setOnClickListener {
            tipText.text = plant.info
            labelCura.setTextColor(Color.WHITE)
            labelInfo.setTextColor(Color.parseColor("#f5bd3d"))
            labelTips.setTextColor(Color.WHITE)
        }
        labelCura.setOnClickListener {
            tipText.text = plant.care
           labelCura.setTextColor(Color.parseColor("#f5bd3d"))
            labelInfo.setTextColor(Color.WHITE)
            labelTips.setTextColor(Color.WHITE)

        }
        labelTips.setOnClickListener {
            tipText.text = plant.tips
            labelCura.setTextColor(Color.WHITE)
            labelInfo.setTextColor(Color.WHITE)
            labelTips.setTextColor(Color.parseColor("#f5bd3d"))
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