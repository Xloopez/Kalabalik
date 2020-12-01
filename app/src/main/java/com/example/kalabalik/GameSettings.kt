package com.example.kalabalik

import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

object GameSettings {

    var playerCount: Int = 0
    var amountOfRounds: Int = 4
    var listOfPlayers: MutableList<Player> = mutableListOf()
    var listOfPlayersHighscore: MutableList<Player> = mutableListOf()
    //var listOfPlayerPoints: MutableList<Player> = mutableListOf()

    fun addPlayerToList(name: String){
        listOfPlayers.add(Player(0, name, 0))
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
    fun highestScoreSorted(listOfPlayer: MutableList<Player>){
        var swap = true

        while(swap){
            swap = false

            for( i in 0 until listOfPlayer.size-1) {
                if (listOfPlayer[i].points < listOfPlayer[i+1].points){
                    var temp = listOfPlayer[i].points
                    listOfPlayer[i].points = listOfPlayer[i+1].points
                    listOfPlayer[i+1].points = temp

                    swap = true
                }
            }
        }
    }
}