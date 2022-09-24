package com.example.didproject.ui

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
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
import com.example.didproject.model.data.UserPlant
import com.example.didproject.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

class AddPlantToGardenFragment : Fragment() {

    //TODO: add image management

    private var _binding: FragmentAddPlantToGardenBinding? = null
    private val binding get() = _binding!!

    private lateinit var getPhotoImage : ActivityResultLauncher<Uri>
    private lateinit var getGalleryImage : ActivityResultLauncher<String>
    private lateinit var profileViewModel : ProfileViewModel
    private lateinit var uriTmp : Uri
    private var lastUri : Uri = Uri.parse("")

    private val possibleLocation : Array<String> = arrayOf("Balcone","Giardino","Interno")
    private var location = -1
    private var dateInMillis : Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddPlantToGardenBinding.inflate(inflater, container, false)

        var plantToEdit : UserPlant

        val root: View = binding.root
        val dateButton = binding.DayButton
        val imageButton = binding.TakeImageButton
        val locationButton = binding.LocationButton
        val plantPersonalImage = binding.PlantPersonalImage
        val plantName = binding.PlantName
        val deleteButton = binding.deletePlantButton
        val nickname = binding.NicknamePlantEdit

        val locationPlaceHolder = binding.plantLocation

        val date = Calendar.getInstance()
        val navController = findNavController()
        val menuHost: MenuHost = requireActivity()

        var pos=0
        val edit=arguments?.getBoolean("edit")?:false

        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        plantName.text = arguments?.getString("name")
        if(edit){
            deleteButton.visibility=View.VISIBLE
            pos=arguments?.getInt("pos")!!
            date.timeInMillis=profileViewModel.user.value?.plants?.get(pos)?.date!!
            location=possibleLocation.indexOf(profileViewModel.user.value?.plants?.get(pos)?.location!!)
            nickname.setText(profileViewModel.user.value?.plants?.get(pos)?.nickname!!)
        }

        getPhotoImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                uriTmp.let { uri ->
                    lastUri=uri
                    Picasso.get().load(uri).fit().centerCrop().into(plantPersonalImage)
                }
            }
        }

        getGalleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent()){
            if(it!=null) {
                lastUri = it
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

        deleteButton.setOnClickListener {
            savePersonalPlantData(edit,true,pos)
        }

        //save in db
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.check_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionbar_check -> {
                        savePersonalPlantData(edit,false,pos)

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

     fun savePersonalPlantData(edit: Boolean, delete:Boolean=false, pos:Int=0) {
         val user = profileViewModel.user.value
         if(location==-1)
             location=0
         val userPlant = UserPlant(arguments?.getString("name")!!,
             dateInMillis,
             binding.NicknamePlantEdit.text.toString(),
             lastUri.toString(),
             possibleLocation[location],
             100)
         if(!edit)
             user?.plants?.add(userPlant)
         else
             if(!delete) {
                 user?.plants?.removeAt(pos)
                 user?.plants?.add(pos,userPlant)
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

                         user?.plants?.removeAt(pos)
                         profileViewModel.updateProfile(user!!)
                         findNavController().navigate(R.id.nav_garden)
                     }
                     .show()

             }
         profileViewModel.updateProfile(user!!)
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