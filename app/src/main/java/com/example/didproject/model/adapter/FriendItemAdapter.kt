package com.example.didproject.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.data.User

class FriendItemAdapter(private var data:List<User>) : RecyclerView.Adapter<FriendItemAdapter.FriendItemViewHolder>() {

    class FriendItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val userCard = v.findViewById<CardView>(R.id.friend_card)
        private val userName: TextView = v.findViewById(R.id.friend_card_name)
        private val userNofPlants: TextView = v.findViewById(R.id.friend_card_n_plants)
//        private val userImage: ImageView = v.findViewById(R.id.friend_card_image)

        fun bind(user: User) {
            userName.text=user.name
            userNofPlants.text=user.plants.size.toString()


            userCard.setOnClickListener {
/*                val bundle = Bundle()
                bundle.putString("name",plant.name)

                Navigation.findNavController(view = it)
                    .navigate(R.id.cataloguePlantFragment, bundle)*/
            }
        }


    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemViewHolder {
            val viewGroup = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.plant_list_item, parent, false)

            return FriendItemViewHolder(viewGroup)
        }

        override fun onBindViewHolder(holder: FriendItemViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount() = data.size


}

