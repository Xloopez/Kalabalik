package com.example.kalabalik

import android.text.Editable

object GameSettings {

    var playerCount: Int = 0
    var listOfPlayers: MutableList<String> = mutableListOf()

    fun addPlayerToList(player: String){


        for (i in 0 until playerCount) {
            listOfPlayers.add(player + "$i")
        }




    }
}