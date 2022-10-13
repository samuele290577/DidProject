package com.example.didproject.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.`object`.PlantCategory
import com.example.didproject.model.adapter.PlantItemAdapter
import com.example.didproject.model.data.Plant
import com.example.didproject.viewmodel.PlantCatalogueViewModel

class PlantListFragment : Fragment(R.layout.fragment_plant_list) {
    private lateinit var plantCatalogueViewModel: PlantCatalogueViewModel
    private lateinit var recyclerView: RecyclerView
    //TODO: low priority, search by scientific name
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plantCatalogueViewModel = ViewModelProvider(requireActivity())[PlantCatalogueViewModel::class.java]
        recyclerView = view.findViewById(R.id.recyclerView_plant_list)

        // configuring recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val plantList = mutableListOf<Plant>()
        initialize(plantList, arguments?.getString(arguments?.getString("key"))!!)
        recyclerView.adapter = PlantItemAdapter(plantList,plantCatalogueViewModel.photoList.value!!)


    }


    private fun initialize(list:MutableList<Plant>, filter:String) {
        list.addAll(plantCatalogueViewModel.getByCategory(filter))
    }
}