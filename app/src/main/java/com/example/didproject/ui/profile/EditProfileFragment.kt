package com.example.didproject.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.didproject.BuildConfig
import com.example.didproject.MainActivity
import com.example.didproject.R
import com.example.didproject.databinding.FragmentProfileEditBinding
import com.example.didproject.model.data.User
import com.example.didproject.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import java.io.File

class EditProfileFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!
    private var lastUri : Uri = Uri.parse("")
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var getPhotoImage : ActivityResultLauncher<Uri>
    private lateinit var getGalleryImage : ActivityResultLauncher<String>
    private lateinit var uriTmp : Uri





        //Part where I populate the view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val name : TextView = binding.nameField
        val nickname : TextView = binding.nicknameField
        val bio : TextView = binding.bioField
        val editImageButton : ImageButton = binding.editImageBtn
        val profilePicture : ImageView = binding.profilePicture

        profileViewModel.user.observe(viewLifecycleOwner) {
            if(it!=null) {
                name.text = it.name
                nickname.text = it.nickname
                bio.text = it.bio
                lastUri = it.imageUri
                Picasso.get().load(it.imageUri).fit().centerCrop().into(profilePicture)
            }
        }

        getPhotoImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                uriTmp?.let { uri ->
                    lastUri=uri
                    Picasso.get().load(uri).fit().centerCrop().into(profilePicture)
                }
            }
        }

        getGalleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent()){
            if(it!=null) {
                lastUri = it
                Picasso.get().load(it).fit().centerCrop().into(profilePicture)
            }
        }

        uriTmp=getTmpFileUri()
        editImageButton.setOnClickListener{ showMenu(editImageButton) }
        return root
    }


    //Here the view is destroyed
    override fun onDestroyView() {
        val profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        val profile=User(
            profileViewModel.user.value?.id?:"prova",
            binding.nameField.text.toString(),
            binding.nicknameField.text.toString(),
            binding.bioField.text.toString(),
            lastUri
            )

        profileViewModel.updateProfile(profile)

        super.onDestroyView()
    }

    private fun showMenu(view: View) {
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