package com.example.kalabalik

class Player(
    var name: String,
    var points: Int = 0,
    var listOfPlayerNamePoints: MutableList<Int> = mutableListOf()
) {
    /*fun addNameAndPoints (player: Player) {
        listOfPlayerNamePoints.add(player)
    }*/
    fun addPointsToPlayer(points: Int) {
        this.points + points
    }
}