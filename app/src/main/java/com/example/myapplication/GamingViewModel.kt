package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GamingViewModel: ViewModel() {

    var updateCardFragment = MutableLiveData<Int>().apply {
        value = 0
    }
    var clearCardFragment = MutableLiveData<Int>().apply {
        value = 0
    }

    var currentCardType = MutableLiveData<EnumUtil.EnRandom>().apply {
        value = EnumUtil.EnRandom.CONSEQUENCES
    }

    var consequencePair = MutableLiveData<Pair<String, Double>>().apply {
        value = Pair("", 0.0)
    }

    var missionPair = MutableLiveData<Pair<String, Double>>().apply {
        value = Pair("", 0.0)
    }

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