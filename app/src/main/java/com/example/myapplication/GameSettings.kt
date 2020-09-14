package com.example.myapplication

object GameSettings {

    var playerCount: Int = 0
    var currentRound: Int = 0
    var amountOfRounds: Int = 15
    var listOfPlayers: MutableList<Player> = mutableListOf()

    fun addPlayerToList(player: Player){
        listOfPlayers.add(player)
    }

    fun getPlayerNameByNum(pNum: Int) : Player? {
        return listOfPlayers.find { player -> player.playerNum == pNum }
    }

    fun totalRounds(): Int {
        return (amountOfRounds.times(playerCount))
    }

}