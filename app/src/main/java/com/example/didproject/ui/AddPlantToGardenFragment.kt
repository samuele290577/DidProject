package com.example.didproject.ui

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var userOriginalPlant : UserPlant
    private val possibleLocation : Array<String> = arrayOf("Balcone","Giardino","Interno")
    private var location = 0
    private var dateInMillis : Long = 0
    private var arduinoSelected = -1
    private lateinit var possibleArduino : Array<String>
    private lateinit var newUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


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
        val dateText : TextView = binding.plantDate
        val plantLocationText : TextView = binding.plantLocation
        val scName : TextView = binding.scName
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
        scName.text = plant?.scName

        if(edit){
            boolCatalogue=false
            deleteButton.visibility=View.VISIBLE
            key=arguments?.getString("key")!!
            userOriginalPlant=profileViewModel.user.value?.plants?.get(key)!!
            val activity=requireActivity() as AppCompatActivity
            activity.supportActionBar?.title="Modifica ${userOriginalPlant.nickname}"
            date.timeInMillis=userOriginalPlant.date
            dateInMillis=date.timeInMillis
            location=possibleLocation.indexOf(userOriginalPlant.location)
            nickname.setText(userOriginalPlant.nickname)
            profileViewModel.personalPlantPhoto.observe(viewLifecycleOwner) {
                if (it.containsKey(key))
                    Picasso.get().load(it[key]).fit().centerCrop().into(plantPersonalImage)
                else
                    boolCatalogue=true
            }
        }
        else{
            val activity=requireActivity() as AppCompatActivity
            activity.supportActionBar?.title="Aggiungi ${plantNameCatalogue}"
            userOriginalPlant= UserPlant()

            nickname.setText(plantNameCatalogue)
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

        dateText.text="${date.get(Calendar.DAY_OF_MONTH)}/${date.get(Calendar.MONTH)+1}/${date.get(Calendar.YEAR)}"
        dateButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(),
                 { _, myear, mmonth, mdayOfMonth ->
                    date.set(myear,mmonth,mdayOfMonth)
                    dateInMillis=date.timeInMillis
                    dateText.text="${mdayOfMonth}/${mmonth+1}/${myear}"
                 }, date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }
        dateInMillis=date.timeInMillis

        plantLocationText.text=possibleLocation[location]
        var locationTmp=location
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
                        0 -> plantLocationText.text=possibleLocation[0]
                        1 -> plantLocationText.text=possibleLocation[1]
                        2 -> plantLocationText.text=possibleLocation[2]
                        else -> {
                            print("no location found error")
                        }
                    }
                }
                .show()
        }

        var arduinoTmp=arduinoSelected
        if(userOriginalPlant.status<=100)
            arduino.visibility = View.GONE
            arduino.setOnClickListener {
                if(possibleArduino.isNotEmpty()) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setIcon(R.drawable.icon_arduino)
                        .setTitle("Collega ad un arduino")
                        .setSingleChoiceItems(possibleArduino, arduinoSelected) { _, which ->
                            arduinoTmp = which
                        }
                        .setNeutralButton("Annulla") { _, _ ->
                            // Respond to neutral button press
                        }
                        .setPositiveButton("Conferma") { _, _ ->
                            // Respond to positive button press
                            arduinoSelected = arduinoTmp
                            if(arduinoSelected!=-1) {
                                arduino.text = "Si collegerà a ${possibleArduino[arduinoSelected]}"
                                arduino.isClickable = false
                            }
                        }
                        .show()
                }
                else {
                    MaterialAlertDialogBuilder(requireContext())
                        .setIcon(R.drawable.icon_arduino)
                        .setTitle("Collega ad un arduino")
                        .setMessage("Non risulta ancora collegato nessun arduino..." +
                                "Se non l'hai ancora collegato segui le istruzioni altrimenti prova a ricollegarlo!")
                        .setPositiveButton("Va bene"){ _,_ ->
                        }
                        .show()
                }
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

                        navController.popBackStack()
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
             userOriginalPlant.status
             )

         if(!edit) {

             user?.plants!![abs(userPlant.hashCode()).toString()]=userPlant
             if(arduinoSelected!=-1)
                 user.arduino[possibleArduino[arduinoSelected]]?.plantIndex= abs(userPlant.hashCode())+1
             if(newUri != Uri.parse(""))
                 profileViewModel.uploadPhoto(newUri, false, abs(userPlant.hashCode()).toString())
         }
         else {
             if (!delete) {
                 user?.plants!![key] = userPlant
                 if (arduinoSelected != -1)
                     user.arduino[possibleArduino[arduinoSelected]]?.plantIndex = key.toInt() + 1
                 if (newUri != Uri.parse(""))
                     profileViewModel.uploadPhoto(newUri, false, key)
             } else {
                 MaterialAlertDialogBuilder(requireContext())
                     .setIcon(R.drawable.delete)
                     .setTitle("Elimina")
                     .setMessage("Vuoi eliminare ${userPlant.nickname} dal tuo giardino?")
                     .setNeutralButton("Annulla") { _, _ ->
                         // Respond to neutral button press

                     }
                     .setPositiveButton("Conferma") { _, _ ->
                         user?.plants?.remove(key)
                         profileViewModel.removePlantImage(key)
                         profileViewModel.updateProfile(user!!, 1)
                         findNavController().popBackStack()
                         findNavController().navigate(R.id.nav_home)
                     }
                     .show()

             }
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