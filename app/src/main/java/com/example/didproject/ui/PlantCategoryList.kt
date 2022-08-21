package com.example.didproject.ui

import android.opengl.Visibility
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.`object`.PlantCategory
import com.example.didproject.model.adapter.PlantCategoryAdapter
import com.example.didproject.model.adapter.PlantItemAdapter
import com.example.didproject.model.data.Plant
import com.example.didproject.viewmodel.PlantCatalogueViewModel


class PlantCategoryList : Fragment(R.layout.fragment_plant_category_list) {

    private lateinit var plantCatalogueViewModel: PlantCatalogueViewModel
    private lateinit var recyclerViewCatalogue: RecyclerView
    private lateinit var recyclerViewPlant: RecyclerView
    private var category: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        plantCatalogueViewModel = ViewModelProvider(requireActivity())[PlantCatalogueViewModel::class.java]
        recyclerViewCatalogue = view.findViewById(R.id.recyclerView_plant_category_list)
        recyclerViewPlant = view.findViewById(R.id.recyclerView_plant_search_list)

        // configuring recyclerview
        recyclerViewCatalogue.layoutManager = LinearLayoutManager(this.context)
        val categoryList = mutableListOf<String>()
        initialize(categoryList)
        recyclerViewCatalogue.adapter = PlantCategoryAdapter(categoryList)

        recyclerViewPlant.layoutManager = LinearLayoutManager(context)

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)

                val sv = menu.findItem(R.id.action_search).actionView as SearchView
                sv.queryHint="Search by name"
                sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String): Boolean {
                        if(newText.isNotEmpty()){
                            recyclerViewCatalogue.alpha=0.5f
                        }
                        else
                            recyclerViewCatalogue.alpha=1f
                        if(newText.length>=3) {
                            //if category show, hide
                            if(recyclerViewCatalogue.isVisible) {
                                recyclerViewCatalogue.visibility = View.GONE
                                recyclerViewPlant.visibility = View.VISIBLE
                            }
                            //display
                            recyclerViewPlant.adapter = PlantItemAdapter(updateSearchRecycleView(newText))
                        }
                        else{
                            if(!recyclerViewCatalogue.isVisible) {
                                recyclerViewCatalogue.visibility = View.VISIBLE
                                recyclerViewPlant.visibility = View.GONE
                            }
                            //if category hide, show
                        }
                        return false
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initialize(list:MutableList<String>) {
            list.add(PlantCategory.ARBUSTI)
            list.add(PlantCategory.AROMATICHE)
            list.add(PlantCategory.BULBOSE)
            list.add(PlantCategory.GRASSE)
            list.add(PlantCategory.INTERNO)
    }

    private fun updateSearchRecycleView(name: String):List<Plant>{
        return plantCatalogueViewModel.getByInputName(name)
    }
}


