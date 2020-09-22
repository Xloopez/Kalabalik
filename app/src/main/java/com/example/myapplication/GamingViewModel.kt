package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GamingViewModel: ViewModel() {

    var currentTurn = MutableLiveData<Int>().apply {
        value = 0
    }
    var currentRound = MutableLiveData<Int>().apply {
        value = 0
    }

    var currentPlayer = MutableLiveData<Player>().apply {
        value = Player("InitialPlayer0", playerNum = 0)
    }

}