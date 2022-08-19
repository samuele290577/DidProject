package com.example.didproject.ui

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
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

        val root: View = binding.root
        val dateButton = binding.DayButton
        val imageButton = binding.TakeImageButton
        val locationButton = binding.LocationButton
        val plantPersonalImage = binding.PlantPersonalImage
        val plantName = binding.PlantName

        val date = Calendar.getInstance()

        val menuHost: MenuHost = requireActivity()

        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        plantName.text = arguments?.getString("name")

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


        locationButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.balcony)
                .setTitle("Posizione")
                .setSingleChoiceItems(possibleLocation,location) { _, which ->
                    location = which
                }
                .setNeutralButton("Annulla") { _, _ ->
                    // Respond to neutral button press
                    location=-1
                }
                .setPositiveButton("Conferma") { _, _ ->
                    // Respond to positive button press
                }
                .show()
        }

        //save in db
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.check_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionbar_check -> {
                        savePersonalPlantData()
                        val navController = findNavController()
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

     fun savePersonalPlantData() {
        val user = profileViewModel.user.value
        val userPlant = UserPlant(arguments?.getString("name")!!,
            dateInMillis,
            binding.NicknamePlantEdit.text.toString(),
            lastUri.toString(),
            possibleLocation[location],
            "ok")

        user?.plants?.add(userPlant)
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