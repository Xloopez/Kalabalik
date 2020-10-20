package com.example.kalabalik

import android.text.Editable
import android.util.Log

object GameSettings {

    var playerCount: Int = 0
    var amountOfRounds: Int = 6
    var listOfPlayers: MutableList<Player> = mutableListOf()
    //var listOfPlayerPoints: MutableList<Player> = mutableListOf()

    fun addPlayerToList(name: String){
        //var name = Player(name = playerName)
        listOfPlayers.add(Player(name))
    }
    fun addPointsToPlayer(counter: Int, points: Int){
        Log.d("!!!", "POÄNG $points")
        //Log.d("!!!", "Counter is: $counter")
        listOfPlayers[counter].points += points
    }
    fun removePointsFromPlayer(counter: Int, points: Int){
        Log.d("!!!", "POÄNG $points")
        listOfPlayers[counter].points -= points
    }
}