package com.example.didproject.ui

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.PopupMenu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.didproject.BuildConfig
import com.example.didproject.R
import com.example.didproject.databinding.FragmentAddPlantToGardenBinding
import com.example.didproject.model.data.Plant
import com.example.didproject.model.data.UserPlant
import com.example.didproject.viewmodel.PlantCatalogueViewModel
import com.example.didproject.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*
import kotlin.math.abs

class AddPlantToGardenFragment : Fragment() {


    private var _binding: FragmentAddPlantToGardenBinding? = null
    private val binding get() = _binding!!

    private lateinit var getPhotoImage : ActivityResultLauncher<Uri>
    private lateinit var getGalleryImage : ActivityResultLauncher<String>
    private lateinit var profileViewModel : ProfileViewModel
    private lateinit var catalogueViewModel : PlantCatalogueViewModel
    private lateinit var uriTmp : Uri

    private lateinit var plant : Plant
    private val possibleLocation : Array<String> = arrayOf("Balcone","Giardino","Interno")
    private var location = -1
    private var dateInMillis : Long = 0
    private var arduinoSelected = -1
    private lateinit var possibleArduino : Array<String>
    private lateinit var newUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //TODO: problems with what the text is showing vs what it is supposed to show + checks

        _binding = FragmentAddPlantToGardenBinding.inflate(inflater, container, false)

        val root: View = binding.root
        val dateButton = binding.DayButton
        val imageButton = binding.TakeImageButton
        val locationButton = binding.LocationButton
        val plantPersonalImage = binding.PlantPersonalImage
        val plantName = binding.PlantName
        val deleteButton = binding.deletePlantButton
        val nickname = binding.NicknamePlantEdit
        val arduino : Button = binding.arduinoButton

        val locationPlaceHolder = binding.plantLocation

        val date = Calendar.getInstance()
        val navController = findNavController()
        val menuHost: MenuHost = requireActivity()

        var key=""
        newUri=Uri.parse("")
        val edit=arguments?.getBoolean("edit")?:false


        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        catalogueViewModel = ViewModelProvider(requireActivity())[PlantCatalogueViewModel::class.java]
        possibleArduino=profileViewModel.getAvailableArduinos()

        var boolCatalogue=true
        val plantNameCatalogue = arguments?.getString("name")
        plantName.text = plantNameCatalogue
        plant=catalogueViewModel.getByName(arguments?.getString("name")!!)
        if(edit){
            boolCatalogue=false
            deleteButton.visibility=View.VISIBLE
            key=arguments?.getString("key")!!
            date.timeInMillis=profileViewModel.user.value?.plants?.get(key)?.date!!
            location=possibleLocation.indexOf(profileViewModel.user.value?.plants?.get(key)?.location!!)
            nickname.setText(profileViewModel.user.value?.plants?.get(key)?.nickname!!)
            profileViewModel.personalPlantPhoto.observe(viewLifecycleOwner) {
                if (it.containsKey(key))
                    Picasso.get().load(it[key]).fit().centerCrop().into(plantPersonalImage)
                else
                    boolCatalogue=true
            }
        }
        if(boolCatalogue){
        catalogueViewModel.photoList.observe(viewLifecycleOwner) {
            if(it.containsKey(plantNameCatalogue))
                Picasso.get().load(it[plantNameCatalogue]).fit().centerCrop().into(plantPersonalImage)
        }}

        getPhotoImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                uriTmp.let { uri ->
                    newUri=uri
                    Picasso.get().load(uri).fit().centerCrop().into(plantPersonalImage)
                }
            }
        }

        getGalleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent()){
            if(it!=null) {
                newUri=it
                Picasso.get().load(it).fit().centerCrop().into(plantPersonalImage)
            }
        }


        dateButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(),
                 { _, myear, mmonth, mdayOfMonth ->
                    date.set(myear,mmonth,mdayOfMonth)
                    dateInMillis=date.timeInMillis
            }, date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }


        var locationTmp=location
        Log.d("Sam location", "locationTmp $locationTmp")
        locationButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.balcony)
                .setTitle("Posizione")
                .setSingleChoiceItems(possibleLocation,location) { _, which ->
                    locationTmp = which
                    Log.d("Sam location", "which $which")
                }
                .setNeutralButton("Annulla") { _, _ ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Conferma") { _, _ ->
                    // Respond to positive button press
                    location=locationTmp
                    when (location){
                        0 -> locationPlaceHolder.text="Balcone"
                        1 -> locationPlaceHolder.text="Giardino"
                        2 -> locationPlaceHolder.text="Interno"
                        else -> {
                            print("no location found error")
                        }
                    }
                }
                .show()
        }

        var arduinoTmp=arduinoSelected
        arduino.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.icon_arduino)
                .setTitle("Collega ad un arduino")
                .setSingleChoiceItems(possibleArduino,arduinoSelected) { _, which ->
                    arduinoTmp = which
                }
                .setNeutralButton("Annulla") { _, _ ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Conferma") { _, _ ->
                    // Respond to positive button press
                    arduinoSelected=arduinoTmp
                }
                .show()
        }

        deleteButton.setOnClickListener {
            savePersonalPlantData(edit,true,key)
        }

        //save in db
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.check_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionbar_check -> {
                        savePersonalPlantData(edit,false,key)

                        navController.navigate(R.id.nav_home)
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        uriTmp=getTmpFileUri()
        imageButton.setOnClickListener{ showMenu(imageButton) }
        return root
    }

     fun savePersonalPlantData(edit: Boolean, delete:Boolean=false, key:String) {
         val user = profileViewModel.user.value
         if(location==-1)
             location=0
         val userPlant = UserPlant(arguments?.getString("name")!!,
             dateInMillis,
             binding.NicknamePlantEdit.text.toString(),
             plant,
             possibleLocation[location],
             100)

         if(!edit) {

             user?.plants!![abs(userPlant.hashCode()).toString()]=userPlant
             if(arduinoSelected!=-1)
                 user.arduino[possibleArduino[arduinoSelected]]?.plantIndex= abs(userPlant.hashCode())+1
             if(newUri != Uri.parse(""))
                 profileViewModel.uploadPhoto(newUri, false, abs(userPlant.hashCode()).toString())
         }
         else
             if(!delete) {
                 user?.plants!![key]=userPlant
                 if(arduinoSelected!=-1)
                     user.arduino[possibleArduino[arduinoSelected]]?.plantIndex= key.toInt()+1
                 if(newUri != Uri.parse(""))
                     profileViewModel.uploadPhoto(newUri, false, key)
             }
            else {
                 MaterialAlertDialogBuilder(requireContext())
                     .setIcon(R.drawable.delete)
                     .setTitle("Elimina")
                     .setMessage("Vuoi eliminare ${userPlant.nickname} dal tuo giardino?")
                     .setNeutralButton("Annulla") { _, _ ->
                         // Respond to neutral button press

                     }
                     .setPositiveButton("Conferma") { _, _ ->

                         user?.plants?.remove(key)
                         profileViewModel.updateProfile(user!!,1)
                         findNavController().navigate(R.id.nav_garden)
                     }
                     .show()

             }
         profileViewModel.updateProfile(user!!,1)
    }

    private fun showMenu(view: View){
        val menu = PopupMenu(activity, view)
        menu.inflate(R.menu.camera_menu)

        menu.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.action_camera -> {
                    takePicture()
                }
                R.id.action_gallery -> {
                    chooseImage()
                }
            }
            true
        }
        menu.show()
    }

    private fun takePicture(){
        getPhotoImage.launch(uriTmp)
    }

    private fun chooseImage(){
        getGalleryImage.launch("image/*")
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }
}