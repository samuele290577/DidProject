package com.example.didproject.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.didproject.R
import com.example.didproject.databinding.FragmentPersonalPlantBinding
import com.example.didproject.model.data.Plant
import com.example.didproject.model.data.User
import com.example.didproject.model.data.UserPlant
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

        val humidity : ImageView = binding.humidityPersonalPlant
        val sun : ImageView = binding.sunPersonalPlant
        val difficulty : ImageView = binding.difficultyPersonalPlant
        val image : ImageView = binding.plantPersonalImage


        val date : TextView = binding.plantDatePersonal
        val location : TextView = binding.plantLocationPersonal

        val labelInfo : TextView = binding.labelInfo
        val labelCura : TextView = binding.labelCura
        val labelTips : TextView = binding.labelConsigli
        val tipText : TextView = binding.tipsPlaceholder
        val statusText : TextView = binding.progressPerc

        val arduinoLabel : FrameLayout = binding.arduinoLabel
        val basicInfo : ConstraintLayout = binding.basicInfo

        tipText.movementMethod = ScrollingMovementMethod()

        val levelOne : Drawable = resources.getDrawable(R.drawable.level_one)
        val levelTwo : Drawable = resources.getDrawable(R.drawable.level_two)
        val levelThree : Drawable = resources.getDrawable(R.drawable.level_three)

        val waterBar : ProgressBar = binding.waterBar
        var userPlant = UserPlant()
        var plant = Plant()
        var key=""



        var plantName=""
        val user : User



        if(arguments?.getString("id").isNullOrEmpty()) {
            user = profileViewModel.user.value!!
            if(catalogueViewModel.getByInputName(arguments?.getString("plantName")!!).isEmpty()){
                key=arguments?.getString("plantName")!!
                userPlant=profileViewModel.user.value?.plants!![key]!!
                plant=userPlant.plant
                plantName=plant.name
            }
            else{
                plant=catalogueViewModel.getByName(arguments?.getString("plantName")!!)
            }
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
            val key = arguments?.getString("plantName")!!
            userPlant=user.plants[key]?:UserPlant()
            plant=userPlant.plant
            plantName=plant.name
            profileViewModel.personalNeighbourPlantPhoto.observe(viewLifecycleOwner) {
                if (it.containsKey(key))
                    Picasso.get().load(it[key]).fit().centerCrop().into(image)
                else {
                    if(catalogueViewModel.photoList.value?.containsKey(plantName)!!)
                        Picasso.get().load(it[plantName]).fit().centerCrop().into(image)
                }
            }
        }

        name.text=plant.name
        scName.text=plant.scName
        tipText.text=plant.info
        location.text=userPlant.location
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = userPlant.date
        date.text =  "Piantata il "+calendar.get(Calendar.DAY_OF_MONTH).toString()+"/"+calendar.get(Calendar.MONTH).toString()


        val status = userPlant.status
        // info visualizzate.
        if(arguments?.getString("id").isNullOrEmpty()){
            profileViewModel.user.observe(viewLifecycleOwner){
                when (it.plants[key]?.status?:101) {
                    101 -> arduinoLabel.visibility = View.GONE
                    else -> {basicInfo.visibility = View.GONE
                        arduinoLabel.visibility = View.VISIBLE
                        waterBar.progress = it.plants[key]?.status!!
                        statusText.text= "$status%"
                    }
                }
            }
        }else{
            when (status) {
                101 -> arduinoLabel.visibility = View.GONE
                else -> {basicInfo.visibility = View.GONE
                    arduinoLabel.visibility = View.VISIBLE
                    waterBar.progress = status
                    statusText.text= "$status%"
                }
            }
        }


        val activity=requireActivity() as AppCompatActivity
        activity.supportActionBar?.title=userPlant.nickname

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



        labelInfo.setOnClickListener {
            tipText.text = plant.info
            labelCura.setTextColor(Color.BLACK)
            labelInfo.setTextColor(Color.parseColor("#0b3b2d"))
            labelTips.setTextColor(Color.BLACK)
        }
        labelCura.setOnClickListener {
            tipText.text = plant.care
            labelCura.setTextColor(Color.parseColor("#0b3b2d"))
            labelInfo.setTextColor(Color.BLACK)
            labelTips.setTextColor(Color.BLACK)

        }
        labelTips.setOnClickListener {
            tipText.text = plant.tips
            labelCura.setTextColor(Color.BLACK)
            labelInfo.setTextColor(Color.BLACK)
            labelTips.setTextColor(Color.parseColor("#0b3b2d"))
        }

        return root
    }
}