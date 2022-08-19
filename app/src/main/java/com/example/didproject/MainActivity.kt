package com.example.didproject

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.*
import com.example.didproject.databinding.ActivityMainBinding
import com.example.didproject.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var profileViewModel : ProfileViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val name = navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_name)
        val email = navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_email)
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        profileViewModel.user.observe(this){
            name.text=it.name
            email.text=FirebaseAuth.getInstance().currentUser?.email
        }

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_signIn -> {
                    startActivity(Intent(this, SignInActivity::class.java), ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                }
            }
            true
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_garden,
                R.id.nav_catalogue,
                R.id.nav_friends,
                R.id.nav_profile
            ), drawerLayout
        )

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}