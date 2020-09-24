package com.example.myapplication

class Player
    (
    var name: String,
    var listOfRoundAndPoints: MutableList<Pair<Int, Double>> = mutableListOf(),
    var playerNum: Int,
    )
{
    fun listAddRoundAndPoints(pair: Pair<Int, Double>){
        listOfRoundAndPoints.add(pair)
    }

    fun sumPointsFromListPair(): Double{
        return listOfRoundAndPoints.sumByDouble { pair -> pair.second }
    }

}