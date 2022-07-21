package com.example.didproject.model.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.getImageResourceId
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PlantCategoryAdapter(private var data:List<String>) : RecyclerView.Adapter<PlantCategoryAdapter.PlantCategoryViewHolder>() {

    class PlantCategoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val card = v.findViewById<CardView>(R.id.category_card)
        private val category: TextView = v.findViewById(R.id.plant_category_name)
        private val categoryImage: ImageView = v.findViewById(R.id.plant_category_image)


        fun bind(entity: String) {
            category.text = entity
            Picasso.get().load(entity.getImageResourceId()).fit().centerCrop().into(categoryImage)

            /*card.setOnClickListener {

            }*/
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

