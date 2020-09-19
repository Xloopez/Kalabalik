package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    var playerCount = MutableLiveData<Int>().apply {
        value = 0
    }
    var amountOfRounds = MutableLiveData<Int>().apply {
        value = 3
    }
    var listOfPlayers = mutableListOf<Player>()

    var currentFragmentPos= MutableLiveData<Int>().apply {
        value = 0
    }

    fun addPlayerToList(player: Player){
        listOfPlayers.add(player)
    }

}