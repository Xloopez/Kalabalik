package com.example.myapplication

object GameSettings
{
    var playerCount: Int = 0
        get() = field
        set(value) {
            field = value
        }

    var amountOfRounds: Int = 15
    var listOfPlayers: MutableList<Player> = mutableListOf()

    fun addPlayerToList(player: Player){
        listOfPlayers.add(player)
    }

}