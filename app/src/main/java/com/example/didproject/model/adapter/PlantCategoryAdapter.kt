package com.example.didproject.model.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.getImageResourceId
import com.squareup.picasso.Picasso

class PlantCategoryAdapter(private var data:List<String>) : RecyclerView.Adapter<PlantCategoryAdapter.PlantCategoryViewHolder>() {

    class PlantCategoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val card = v.findViewById<CardView>(R.id.category_card)
        private val category: TextView = v.findViewById(R.id.plant_category_name)
        private val categoryImage: ImageView = v.findViewById(R.id.plant_category_image)


        fun bind(entity: String) {
            category.text = entity
            Picasso.get().load(entity.getImageResourceId()).fit().centerCrop().into(categoryImage)

            card.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("key", "category")
                bundle.putString("category",entity)

                Navigation.findNavController(view = it)
                    .navigate(R.id.plantListFragment, bundle)
            }
        }


    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantCategoryViewHolder {
            val viewGroup = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.plant_category_list_item, parent, false)

            return PlantCategoryViewHolder(viewGroup)
        }

        override fun onBindViewHolder(holder: PlantCategoryViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount() = data.size

}

