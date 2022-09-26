package com.example.didproject.model.adapter

import android.net.Uri
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
import com.example.didproject.model.data.Plant
import com.example.didproject.model.data.UserPlant
import com.squareup.picasso.Picasso

class PersonalPlantItemAdapter(private var data:List<Plant>, private var auxData:List<UserPlant>, private var userId: String) : RecyclerView.Adapter<PersonalPlantItemAdapter.PersonalPlantItemViewHolder>() {


    class PersonalPlantItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val plantCard = v.findViewById<CardView>(R.id.personal_plant_card)
        private val plantName: TextView = v.findViewById(R.id.personal_plant_card_name)
        private val plantScientificName: TextView = v.findViewById(R.id.personal_plant_card_scientific_name)
        private val plantDifficulty: TextView = v.findViewById(R.id.personal_plant_card_difficulty)
        private val plantHumidity: TextView = v.findViewById(R.id.personal_plant_card_humidity)
        private val plantSun: TextView = v.findViewById(R.id.personal_plant_card_sun)
        private val plantImage: ImageView = v.findViewById(R.id.personal_plant_card_image)

        fun bind(plant: Plant, userPlant: UserPlant, userId: String) {
            plantName.text=userPlant.nickname
            plantScientificName.text=plant.scName
            plantDifficulty.text=plant.difficulty.toString()
            plantSun.text=plant.sun.toString()
            plantHumidity.text=plant.humidity.toString()
            Picasso.get().load(Uri.parse(userPlant.customPhoto)).fit().centerCrop().into(plantImage)


            plantCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("plantName",plant.name)
                bundle.putString("id", userId)

                Navigation.findNavController(view = it)
                    .navigate(R.id.personalPlantFragment, bundle)
            }
        }


    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalPlantItemViewHolder {
            val viewGroup = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.personal_plant_list_item, parent, false)

            return PersonalPlantItemViewHolder(viewGroup)
        }

        override fun onBindViewHolder(holder: PersonalPlantItemViewHolder, position: Int) {
            holder.bind(data[position], auxData[position], userId)
        }

        override fun getItemCount() = data.size


}

