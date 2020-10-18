package com.example.kalabalik

class Player(
    var name: String,
    var points: Int = 0,
    var listOfPlayerNamePoints: MutableList<Int> = mutableListOf()
)