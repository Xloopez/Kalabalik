package com.example.kalabalik

class Player(
    var name: String,
    var points: Int = 0,
    var listPlayerNamePoints: MutableList<Player> = mutableListOf(),
){
    fun addNameAndPoints (player: Player){
        listPlayerNamePoints.add(player)
    }
    fun addPointsToPlayer(points: Int){
        this.points + points
    }
}