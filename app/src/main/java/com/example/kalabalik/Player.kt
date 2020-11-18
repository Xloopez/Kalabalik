package com.example.kalabalik

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


class Player(
    var name: String = "",
    var points: Int = 0,
    //var listOfPlayerNamePoints: MutableList<Int> = mutableListOf()
)