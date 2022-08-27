package com.example.didproject.model.adapter

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

class PlantItemAdapter(private var data:List<Plant>) : RecyclerView.Adapter<PlantItemAdapter.PlantItemViewHolder>() {

    class PlantItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val plantCard = v.findViewById<CardView>(R.id.plant_card)
        private val plantName: TextView = v.findViewById(R.id.plant_card_name)
        private val plantScientificName: TextView = v.findViewById(R.id.plant_card_scientific_name)
        private val plantDifficulty: TextView = v.findViewById(R.id.plant_card_difficulty)
        private val plantHumidity: TextView = v.findViewById(R.id.plant_card_humidity)
        private val plantSun: TextView = v.findViewById(R.id.plant_card_sun)
        private val plantImage: ImageView = v.findViewById(R.id.plant_card_image)

        fun bind(plant: Plant) {
            plantName.text=plant.name
            plantScientificName.text=plant.scName
            plantDifficulty.text=plant.difficulty.toString()
            plantSun.text=plant.sun.toString()
            plantHumidity.text=plant.humidity.toString()
            Picasso.get().load(Uri.parse(plant.photo)).fit().centerCrop().into(plantImage)


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
            holder.bind(data[position])
        }

        override fun getItemCount() = data.size


}

