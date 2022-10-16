package com.example.didproject.ui

import android.os.Bundle
import android.view.*
import android.widget.ImageView
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
        val info : TextView = binding.plantInfoPersonal
        val care : TextView = binding.plantCarePersonal
        val tips : TextView = binding.plantTipPersonal
        val humidity : TextView = binding.humidityPersonalPlant
        val sun : TextView = binding.sunPersonalPlant
        val status : TextView = binding.statusPersonalPlant
        val image : ImageView = binding.plantPersonalImage

        val date : TextView = binding.plantDatePersonal
        val location : TextView = binding.plantLocationPersonal

        var boolCatalogue=true

        var user : User
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

        //????????????????
        val userPlant = user.plants.values.first { it.plantName == arguments?.getString("plantName")!! }
        location.text=userPlant.location
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = userPlant.date
        date.text =  "Piantata il "+calendar.get(Calendar.DAY_OF_MONTH).toString()+"/"+calendar.get(Calendar.MONTH).toString()


        val plant = catalogueViewModel.getByName(arguments?.getString("plantName")!!)
        name.text=plant.name
        scName.text=plant.scName
        info.text=plant.info
        tips.text=plant.tips
        care.text=plant.care
        humidity.text=plant.humidity.toString()
        sun.text=plant.sun.toString()
        return root
    }
}