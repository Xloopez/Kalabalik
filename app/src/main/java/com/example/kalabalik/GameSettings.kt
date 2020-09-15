package com.example.kalabalik

import android.text.Editable

object GameSettings {

    ///
    var playerCount: Int = 0
    var listOfPlayers: MutableList<String> = mutableListOf()

    fun addPlayerToList(player: String){
            listOfPlayers.add(player)
    }
}