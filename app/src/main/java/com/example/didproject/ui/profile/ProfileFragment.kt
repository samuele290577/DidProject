package com.example.didproject.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.didproject.R
import com.example.didproject.databinding.FragmentProfileBinding
import com.example.didproject.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        val menuHost: MenuHost = requireActivity()

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val name : TextView = binding.nameField
        val nickname : TextView = binding.nicknameField
        val email : TextView = binding.emailField
        val bio : TextView = binding.bioField
        val profilePicture = binding.profilePicture

        profileViewModel.user.observe(viewLifecycleOwner){
            if(it!=null){
                name.text = it.name
                nickname.text = it.nickname
                bio.text = it.bio
                Picasso.get().load(Uri.parse(it.imageUri)).fit().centerCrop().into(profilePicture)
            }
        }
        email.text = FirebaseAuth.getInstance().currentUser?.email?:"none"

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.edit, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.actionbar_edit -> {
                        val navController = findNavController()
                        navController.navigate(R.id.editProfileFragment)
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return root
    }

}