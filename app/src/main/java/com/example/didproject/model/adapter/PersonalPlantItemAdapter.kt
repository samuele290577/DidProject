package com.example.didproject.model.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
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
    private var data: HashMap<String, UserPlant>,
    private var images: HashMap<String, Uri>,
    private var keyList: List<String>,
    private var userId: String
) : RecyclerView.Adapter<PersonalPlantItemAdapter.PersonalPlantItemViewHolder>() {


    class PersonalPlantItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val plantCard = v.findViewById<CardView>(R.id.personal_plant_card)
        private val plantName: TextView = v.findViewById(R.id.personal_plant_card_name)
        private val plantScientificName: TextView =
            v.findViewById(R.id.personal_plant_card_scientific_name)
        private val plantDifficulty: ImageView = v.findViewById(R.id.personal_plant_card_difficulty)
        private val plantHumidity: ImageView = v.findViewById(R.id.personal_plant_card_humidity)
        private val plantSun: ImageView = v.findViewById(R.id.personal_plant_card_sun)
        private val plantImage: ImageView = v.findViewById(R.id.personal_plant_card_image)
        private val plantStatus: RelativeLayout = v.findViewById(R.id.status_color)


        val levelOne : Drawable = v.context.resources.getDrawable(R.drawable.level_one)
        val levelTwo : Drawable = v.context.resources.getDrawable(R.drawable.level_two)
        val levelThree : Drawable = v.context.resources.getDrawable(R.drawable.level_three)

        fun bind(userPlant: UserPlant, image: Uri,key:String, userId: String) {
            plantName.text = userPlant.nickname
            plantScientificName.text = userPlant.plant.scName

            when (userPlant.plant.humidity){
                1 -> plantHumidity.setImageDrawable(levelOne)
                2 -> plantHumidity.setImageDrawable(levelTwo)
                else -> {
                    plantHumidity.setImageDrawable(levelThree)
                }
            }
            when (userPlant.plant.difficulty){
                1 -> plantDifficulty.setImageDrawable(levelOne)
                2 -> plantDifficulty.setImageDrawable(levelTwo)
                else -> {
                    plantDifficulty.setImageDrawable(levelThree)
                }
            }
            when (userPlant.plant.sun){
                1 -> plantSun.setImageDrawable(levelOne)
                2 -> plantSun.setImageDrawable(levelTwo)
                else -> {
                    plantSun.setImageDrawable(levelThree)
                }
            }

            if (userId == "") {
                if(userPlant.status>100)
                    plantStatus.visibility=View.GONE
                else if (userPlant.status in 50 .. 100)
                    plantStatus.setBackgroundColor(Color.parseColor("#386555"))
                else if (userPlant.status in 25..51)
                    plantStatus.setBackgroundColor(Color.parseColor("#f5bd3d"))
                else
                    plantStatus.setBackgroundColor(Color.parseColor("#ff00"))
            }
            else{
                plantStatus.visibility=View.GONE
            }
            Picasso.get().load(image).fit().centerCrop().into(plantImage)


            plantCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("plantName", key)
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
        holder.bind(data[keyList[position]]?: UserPlant(), images[keyList[position]]?:Uri.parse(""), keyList[position], userId)
    }

    override fun getItemCount() = keyList.size

}

