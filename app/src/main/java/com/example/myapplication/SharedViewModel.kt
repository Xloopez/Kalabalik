package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    var playerCount: Int = 0
    var amountOfRounds: Int = 3 //15
    var listOfPlayers = mutableListOf<Player>()

    var currentFragmentPos= MutableLiveData<Int>().apply {
        value = 0
    }

}