package com.example.kalabalik

import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

object GameSettings {

    var playerCount: Int = 0
    var amountOfRounds: Int = 4
    var listOfPlayers: MutableList<Player> = mutableListOf()
    var listOfPlayersHighscore: List<Item> = listOf()
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
        listOfPlayers[counter].points += points
    }

    /*fun highestScoreSorted(){
        var swap = true

        while(swap){
            swap = false

            for( i in 1 until listOfPlayers.size-1) {
                if (listOfPlayers[i].points < listOfPlayers[i+1].points){
                    var temp = listOfPlayers[i].points
                    listOfPlayers[i].points = listOfPlayers[i+1].points
                    listOfPlayers[i+1].points = temp

                    swap = true
                }
            }
        }
    }*/
}