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
import com.example.didproject.model.getImageResourceId
import com.squareup.picasso.Picasso

class HomepageItemAdapter(private var data:List<String>, private var imageData: List<Uri>, private var bundleData:List<String>, private var plants:Boolean) : RecyclerView.Adapter<HomepageItemAdapter.HomepageItemViewHolder>() {

    //TODO add images in base of the type

    class HomepageItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val card = v.findViewById<CardView>(R.id.homepage_card)
        private val itemName: TextView = v.findViewById(R.id.homepage_card_name)
        private val itemImage: ImageView = v.findViewById(R.id.homepage_card_image)


        fun bind(name: String, imageData:Uri, bundleData:String, plants:Boolean) {
            itemName.text = name

            Picasso.get().load(imageData).fit().centerCrop().into(itemImage)

            card.setOnClickListener {
                val bundle = Bundle()
                if (plants) {
                    bundle.putString("plantName", bundleData)

                    Navigation.findNavController(view = it)
                        .navigate(R.id.personalPlantFragment, bundle)
                }
                else{
                    bundle.putString("id", bundleData)
                    Navigation.findNavController(view = it)
                        .navigate(R.id.friendProfileFragment, bundle)
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
            holder.bind(data[position], imageData[position], bundleData[position], plants)
        }

        override fun getItemCount() = data.size

}

