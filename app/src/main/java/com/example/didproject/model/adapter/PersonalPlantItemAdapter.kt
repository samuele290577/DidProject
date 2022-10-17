package com.example.didproject.model.adapter

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.data.UserPlant
import com.squareup.picasso.Picasso

class PersonalPlantItemAdapter(
    private var auxData: HashMap<String, UserPlant>,
    private var images: HashMap<String, Uri>,
    private var userId: String
) : RecyclerView.Adapter<PersonalPlantItemAdapter.PersonalPlantItemViewHolder>() {


    class PersonalPlantItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val plantCard = v.findViewById<CardView>(R.id.personal_plant_card)
        private val plantName: TextView = v.findViewById(R.id.personal_plant_card_name)
        private val plantScientificName: TextView =
            v.findViewById(R.id.personal_plant_card_scientific_name)
        private val plantDifficulty: TextView = v.findViewById(R.id.personal_plant_card_difficulty)
        private val plantHumidity: TextView = v.findViewById(R.id.personal_plant_card_humidity)
        private val plantSun: TextView = v.findViewById(R.id.personal_plant_card_sun)
        private val plantImage: ImageView = v.findViewById(R.id.personal_plant_card_image)
        private val plantStatus: RelativeLayout = v.findViewById(R.id.status_color)

        fun bind(userPlant: UserPlant, image: Uri, userId: String) {
            plantName.text = userPlant.nickname
            plantScientificName.text = userPlant.plant.scName
            plantDifficulty.text = userPlant.plant.difficulty.toString()
            plantSun.text = userPlant.plant.sun.toString()
            plantHumidity.text = userPlant.plant.humidity.toString()
            if (userId == "") {
                if (userPlant.status > 50)
                    plantStatus.setBackgroundColor(Color.parseColor("#386555"))
                else if (userPlant.status in 25..51)
                    plantStatus.setBackgroundColor(Color.parseColor("#f5bd3d"))
                else
                    plantStatus.setBackgroundColor(Color.parseColor("#FF000000"))
            }
            Picasso.get().load(image).fit().centerCrop().into(plantImage)


            plantCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("plantName", userPlant.plant.name)
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
        holder.bind(auxData.values.toList()[position], images.values.toList()[position], userId)
    }

    override fun getItemCount() = auxData.size

}

