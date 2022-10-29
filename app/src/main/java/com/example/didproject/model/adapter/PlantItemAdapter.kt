package com.example.didproject.model.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.data.Plant
import com.squareup.picasso.Picasso

class PlantItemAdapter(private var data:List<Plant>, private var photoMap :Map<String,Uri>) : RecyclerView.Adapter<PlantItemAdapter.PlantItemViewHolder>() {

    class PlantItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val plantCard = v.findViewById<CardView>(R.id.plant_card)
        private val plantName: TextView = v.findViewById(R.id.plant_card_name)
        private val plantScientificName: TextView = v.findViewById(R.id.plant_card_scientific_name)
        private val plantDifficulty: ImageView = v.findViewById(R.id.plant_card_difficulty)
        private val plantHumidity: ImageView = v.findViewById(R.id.plant_card_humidity)
        private val plantSun: ImageView = v.findViewById(R.id.plant_card_sun)
        private val plantImage: ImageView = v.findViewById(R.id.plant_card_image)

        val levelOne : Drawable = v.context.resources.getDrawable(R.drawable.level_one)
        val levelTwo : Drawable = v.context.resources.getDrawable(R.drawable.level_two)
        val levelThree : Drawable = v.context.resources.getDrawable(R.drawable.level_three)

        fun bind(plant: Plant, photo:Uri) {
            plantName.text=plant.name
            plantScientificName.text=plant.scName

            when (plant.humidity){
                1 -> plantHumidity.setImageDrawable(levelOne)
                2 -> plantHumidity.setImageDrawable(levelTwo)
                else -> {
                    plantHumidity.setImageDrawable(levelThree)
                }
            }
            when (plant.difficulty){
                1 -> plantDifficulty.setImageDrawable(levelOne)
                2 -> plantDifficulty.setImageDrawable(levelTwo)
                else -> {
                    plantDifficulty.setImageDrawable(levelThree)
                }
            }
            when (plant.sun){
                1 -> plantSun.setImageDrawable(levelOne)
                2 -> plantSun.setImageDrawable(levelTwo)
                else -> {
                    plantSun.setImageDrawable(levelThree)
                }
            }
            Picasso.get().load(photo).fit().centerCrop().into(plantImage)


            plantCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("name",plant.name)

                Navigation.findNavController(view = it)
                    .navigate(R.id.cataloguePlantFragment, bundle)
            }
        }


    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantItemViewHolder {
            val viewGroup = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.plant_list_item, parent, false)

            return PlantItemViewHolder(viewGroup)
        }

        override fun onBindViewHolder(holder: PlantItemViewHolder, position: Int) {
            holder.bind(data[position], photoMap[data[position].name]?:Uri.parse(""))
        }

        override fun getItemCount() = data.size


}

