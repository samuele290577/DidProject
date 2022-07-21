package com.example.didproject.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.`object`.PlantCategory
import com.example.didproject.model.adapter.PlantCategoryAdapter


class PlantCategoryList : Fragment(R.layout.fragment_plant_category_list) {

    private lateinit var recyclerView: RecyclerView
    private var category: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView_plant_category_list)

        // configuring recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        var categoryList = mutableListOf<String>()
        initialize(categoryList)
        recyclerView.adapter = PlantCategoryAdapter(categoryList)
    }


    private fun initialize(list:MutableList<String>) {
        list.add(PlantCategory.ARBUSTI)
        list.add(PlantCategory.AROMATICHE)
        list.add(PlantCategory.BULBOSE)
        list.add(PlantCategory.GRASSE)
        list.add(PlantCategory.INTERNO)
    }
}