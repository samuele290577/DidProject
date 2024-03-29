package com.example.didproject

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.didproject.databinding.ActivityMainBinding
import com.example.didproject.viewmodel.PlantCatalogueViewModel
import com.example.didproject.viewmodel.ProfileViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var profileViewModel : ProfileViewModel
    private lateinit var plantViewModel : PlantCatalogueViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        plantViewModel = ViewModelProvider(this)[PlantCatalogueViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val name = navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_name)
        val email = navView.getHeaderView(0).findViewById<TextView>(R.id.drawer_email)
        val image = navView.getHeaderView(0).findViewById<ImageView>(R.id.drawer_image)
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        profileViewModel.user.observe(this){
            name.text=it.name
            email.text=FirebaseAuth.getInstance().currentUser?.email
        }
        profileViewModel.photo.observe(this){
            Picasso.get().load(it).fit().centerCrop().into(image)
        }


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_signIn -> {
                    startActivity( Intent(this, SignInActivity::class.java),ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
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
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}