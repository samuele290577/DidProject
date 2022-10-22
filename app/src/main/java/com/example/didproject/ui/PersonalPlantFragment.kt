package com.example.didproject.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.didproject.R
import com.example.didproject.databinding.FragmentPersonalPlantBinding
import com.example.didproject.model.data.User
import com.example.didproject.viewmodel.FriendViewModel
import com.example.didproject.viewmodel.PlantCatalogueViewModel
import com.example.didproject.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import java.util.*


//TODO: assegnare valore status alla progressBar e gestire il colore della progress bar in base al numero passato alla progress bar
//TODO: visualizzazione condizionale del box di arduino.
//TODO: cosa visualizzare alla sinistra?
class PersonalPlantFragment : Fragment() {
    private var _binding: FragmentPersonalPlantBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var catalogueViewModel: PlantCatalogueViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        friendViewModel = ViewModelProvider(requireActivity())[FriendViewModel::class.java]
        catalogueViewModel = ViewModelProvider(requireActivity())[PlantCatalogueViewModel::class.java]
        _binding = FragmentPersonalPlantBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()
        val root: View = binding.root

        val name : TextView = binding.plantNamePersonal
        val scName : TextView = binding.plantScnamePersonal
        val humidity : TextView = binding.humidityPersonalPlant
        val sun : TextView = binding.sunPersonalPlant
        binding.statusPersonalPlant
        val image : ImageView = binding.plantPersonalImage

        val date : TextView = binding.plantDatePersonal
        val location : TextView = binding.plantLocationPersonal

        val labelInfo : TextView = binding.labelInfo
        val labelCura : TextView = binding.labelCura
        val labelTips : TextView = binding.labelConsigli
        val tipText : TextView = binding.tipsPlaceholder

        val waterBar : ProgressBar = binding.waterBar

        val plant = catalogueViewModel.getByName(arguments?.getString("plantName")!!)
        name.text=plant.name
        scName.text=plant.scName
        tipText.text=plant.info
        humidity.text=plant.humidity.toString()
        sun.text=plant.sun.toString()

        labelInfo.setOnClickListener {
            Log.d("clicked", "clicked")
            tipText.text = plant.info
            labelCura.setTextColor(Color.BLACK)
            labelInfo.setTextColor(Color.GREEN)
            labelTips.setTextColor(Color.WHITE)
        }
        labelCura.setOnClickListener {
            Log.d("clicked", "clicked")
            tipText.text = plant.care
            labelCura.setTextColor(Color.GREEN)
            labelInfo.setTextColor(Color.BLACK)
            labelTips.setTextColor(Color.BLACK)

        }
        labelTips.setOnClickListener {
            Log.d("clicked", "clicked")
            tipText.text = plant.tips
            labelCura.setTextColor(Color.BLACK)
            labelInfo.setTextColor(Color.BLACK)
            labelTips.setTextColor(Color.GREEN)
        }





        val user : User
        val plantName=arguments?.getString("plantName")!!

        if(arguments?.getString("id").isNullOrEmpty()) {
            user = profileViewModel.user.value!!


            val key = user.plants.filter {
                it.value.plantName == plantName
            }.keys.first()

            profileViewModel.personalPlantPhoto.observe(viewLifecycleOwner) {
                if (it.containsKey(key))
                    Picasso.get().load(it[key]).fit().centerCrop().into(image)
                else {
                    if(catalogueViewModel.photoList.value?.containsKey(plantName)!!)
                        Picasso.get().load(it[plantName]).fit().centerCrop().into(image)
                }
            }

            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.edit, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.actionbar_edit -> {
                            val navController = findNavController()
                            val bundle = Bundle()
                            bundle.putBoolean("edit",true)
                            bundle.putString("name",plantName)

                            bundle.putString("key",key)
                            navController.navigate(R.id.addPlantToGardenFragment,bundle)
                            return true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
        else{
            user = friendViewModel.searchUserById(arguments?.getString("id")!!)
            val key = user.plants.filter {
                it.value.plantName == plantName
            }.keys.first()
            profileViewModel.personalNeighbourPlantPhoto.observe(viewLifecycleOwner) {
                if (it.containsKey(key))
                    Picasso.get().load(it[key]).fit().centerCrop().into(image)
                else {
                    if(catalogueViewModel.photoList.value?.containsKey(plantName)!!)
                        Picasso.get().load(it[plantName]).fit().centerCrop().into(image)
                }
            }
        }

        val userPlant = user.plants.values.first { it.plantName == arguments?.getString("plantName")!! }
        location.text=userPlant.location
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = userPlant.date
        date.text =  "Piantata il "+calendar.get(Calendar.DAY_OF_MONTH).toString()+"/"+calendar.get(Calendar.MONTH).toString()



        return root
    }
}