package com.example.myapplication

object GameSettings {

    var playerCount: Int = 0
//    var currentRound: Int = 0
    var amountOfRounds: Int = 3 //15
    var listOfPlayers: MutableList<Player> = mutableListOf()

    fun addPlayerToList(player: Player){
        listOfPlayers.add(player)
    }

    fun totalRounds(): Int {
        return (amountOfRounds.times(playerCount))
    }

}