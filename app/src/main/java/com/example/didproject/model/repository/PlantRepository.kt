package com.example.didproject.model.repository

import com.example.didproject.model.data.Plant
import com.google.firebase.database.DataSnapshot
import kotlin.collections.ArrayList

class PlantRepository (var db: DataSnapshot) {


        fun getPlantById(id:String): Plant {
            return db.child(id).getValue(Plant::class.java)!!
        }

        fun getPlantsByName(name:String): List<Plant> {
            val filteredList=ArrayList<Plant>()
            val reg=("$name.").toRegex()

            db.children.filter { a ->
                reg.matches(a.child(name).getValue(String::class.java)!!)
            }.forEach{
                a-> filteredList.add(a.getValue(Plant::class.java)!!)
            }
            return  filteredList
        }

        fun findByType(type:String):List<Plant>{
            val filteredList=ArrayList<Plant>()
            db.children.filter {
                    a -> a.hasChild(type)
            }.forEach {
                    a -> filteredList.add(a.getValue(Plant::class.java)!!)
            }
            return filteredList
        }
}