package com.example.didproject.model.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.didproject.R
import com.example.didproject.model.data.User


class FriendItemAdapter(private var data:List<User>, private var newFriend:Boolean) : RecyclerView.Adapter<FriendItemAdapter.FriendItemViewHolder>() {

    class FriendItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val userCard = v.findViewById<CardView>(R.id.friend_card)
        private val userName: TextView = v.findViewById(R.id.friend_card_name)
        private val userNickname: TextView = v.findViewById(R.id.friend_card_nickname)
        private val userNofPlants: TextView = v.findViewById(R.id.friend_profile_n_of_plants)
//        private val userImage: ImageView = v.findViewById(R.id.friend_card_image)

        fun bind(user: User, newFriend: Boolean) {
            userName.text=user.name
            userNickname.text=user.nickname
            userNofPlants.text=user.plants.size.toString()

            userCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", user.id)
                bundle.putBoolean("new", newFriend)
                Navigation.findNavController(view = it)
                    .navigate(R.id.friendProfileFragment, bundle)
            }
        }


    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemViewHolder {
            val viewGroup = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.friend_list_item, parent, false)

            return FriendItemViewHolder(viewGroup)
        }

        override fun onBindViewHolder(holder: FriendItemViewHolder, position: Int) {
            holder.bind(data[position], newFriend)
        }

        override fun getItemCount() = data.size


}

