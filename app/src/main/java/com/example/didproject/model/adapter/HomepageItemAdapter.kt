package com.example.didproject.model.adapter

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.data.Neighbour
import com.example.didproject.model.data.UserPlant
import com.squareup.picasso.Picasso

class HomepageItemAdapter(private var keys:List<String>, private var imageData: Map<String,Uri>, private var arePlants:Boolean,private var plants:Map<String,UserPlant> = mapOf(), private var neighbours :Map<String, Neighbour> = mapOf()) : RecyclerView.Adapter<HomepageItemAdapter.HomepageItemViewHolder>() {

    class HomepageItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val card = v.findViewById<CardView>(R.id.homepage_card)
        private val itemName: TextView = v.findViewById(R.id.homepage_card_name)
        private val itemImage: ImageView = v.findViewById(R.id.homepage_card_image)
        private val itemLayout: LinearLayout = v.findViewById(R.id.homepage_linear_layout)


        fun bind(name: String, imageData:Uri, bundleData:String, plants:Boolean, status:Int=100) {
            itemName.text = name

            if(status>50)
                itemLayout.setBackgroundColor(Color.parseColor("#386555"))
            else if(status in 25..51)
                itemLayout.setBackgroundColor(Color.parseColor("#f5bd3d"))
            else
                itemLayout.setBackgroundColor(Color.parseColor("#FF000000"))


            Picasso.get().load(imageData).fit().centerCrop().into(itemImage)

            card.setOnClickListener {
                val navController = Navigation.findNavController(view = it)
                val bundle = Bundle()
                if (plants) {
                    bundle.putString("plantName", bundleData)

                        navController.navigate(R.id.personalPlantFragment, bundle)
                    Navigation
                }
                else{
                    bundle.putString("id", bundleData)
                    navController.navigate(R.id.friendProfileFragment, bundle)
                }
            }
        }


    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomepageItemViewHolder {
            val viewGroup = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.homepage_list_item, parent, false)

            return HomepageItemViewHolder(viewGroup)
        }

        override fun onBindViewHolder(holder: HomepageItemViewHolder, position: Int) {
            if(arePlants) {
                    holder.bind(
                        plants[keys[position]]?.nickname?:"no name",
                        imageData[keys[position]]?: Uri.parse(""),
                        plants[keys[position]]?.plantName?:"",
                        arePlants,
                        plants[keys[position]]?.status?:100)
            }
            else {
                    holder.bind(
                        neighbours[keys[position]]?.nickname?:"no name",
                        imageData[keys[position]]?: Uri.parse(""),
                        neighbours[keys[position]]?.id?:"",
                        arePlants)
            }
        }

        override fun getItemCount()=keys.size



}

