package com.example.didproject.ui.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.didproject.R
import com.example.didproject.databinding.FragmentProfileEditBinding
import com.example.didproject.model.data.User
import com.example.didproject.viewmodel.ProfileViewModel

class EditProfileFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel




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

        profileViewModel.profile.observe(viewLifecycleOwner) {
            if(it!=null) {
                name.text = it.name
                nickname.text = it.nickname
                bio.text = it.bio
            }
        }

        editImageButton.setOnClickListener{ showMenu(editImageButton) }
        return root
    }


    //Here the view is destroyed
    override fun onDestroyView() {
        val profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        val profile=User(
            profileViewModel.profile.value?.id?:"prova",
            binding.nameField.text.toString(),
            binding.nicknameField.text.toString(),
            binding.bioField.text.toString()
            )

        profileViewModel.updateProfile(profile)

        super.onDestroyView()
    }

    private fun showMenu(view: View) {
        val menu = PopupMenu(activity, view)
        menu.inflate(R.menu.camera_menu)

        menu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            true
        })
        menu.show()
    }






}