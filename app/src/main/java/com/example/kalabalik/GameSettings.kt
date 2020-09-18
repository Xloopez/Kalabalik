package com.example.kalabalik

import android.text.Editable

object GameSettings {

    var playerCount: Int = 0
    var amountOfRounds: Int = 15
    var listOfPlayers: MutableList<String> = mutableListOf()

    fun addPlayerToList(name: String){
        //var name = Player(name = playerName)
        listOfPlayers.add(name)
    }
}