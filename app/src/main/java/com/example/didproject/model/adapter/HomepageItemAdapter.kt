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
import com.squareup.picasso.Picasso

class HomepageItemAdapter(private var data:List<String>, private var imageData: List<Uri>, private var bundleData:List<String>, private var plants:Boolean, private val status:List<Int> = listOf()) : RecyclerView.Adapter<HomepageItemAdapter.HomepageItemViewHolder>() {

    class HomepageItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val card = v.findViewById<CardView>(R.id.homepage_card)
        private val itemName: TextView = v.findViewById(R.id.homepage_card_name)
        private val itemImage: ImageView = v.findViewById(R.id.homepage_card_image)
        private val itemLayout: LinearLayout = v.findViewById(R.id.homepage_linear_layout)


        fun bind(name: String, imageData:Uri, bundleData:String, plants:Boolean, status:Int) {
            itemName.text = name

            if(status>50)
                itemLayout.setBackgroundColor(Color.parseColor("#0b3b2d"))
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
            if(plants) {
                if (position >= imageData.size)
                    holder.bind(
                        data[position],
                        Uri.parse(""),
                        bundleData[position],
                        plants,
                        status[position]
                    )
                else
                    holder.bind(
                        data[position],
                        imageData[position],
                        bundleData[position],
                        plants,
                        status[position]
                    )
            }
            else {
                if (position >= imageData.size)
                    holder.bind(
                        data[position],
                        Uri.parse(""),
                        bundleData[position],
                        plants,
                        100
                    )
                else
                    holder.bind(
                        data[position],
                        imageData[position],
                        bundleData[position],
                        plants,
                        100
                    )
            }
        }

        override fun getItemCount() = data.size

}

