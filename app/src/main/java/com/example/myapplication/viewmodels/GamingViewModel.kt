package com.example.myapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.dataclasses.Player
import com.example.myapplication.TimedTask
import com.example.myapplication.utilities.EnumUtil

class GamingViewModel : ViewModel() {

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

    var timedTaskCard = MutableLiveData<TimedTask>()

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

    fun updateRound() = currentRound.postValue(currentRound.value?.plus(1))
    fun updateTurn()  = currentTurn.postValue(currentTurn.value?.plus(1))
    fun updatePlayer(player: Player) = currentPlayer.postValue(player)
    fun clearCard() = clearCardFragment.postEmpty()

    fun updateCurrentCard(t: Triple<String, Double, EnumUtil.EnRandom>){
        when (t.third) {
            EnumUtil.EnRandom.CONSEQUENCES -> consequencePair.postValue(Pair(t.first, t.second))
            EnumUtil.EnRandom.MISSION -> missionPair.postValue(Pair(t.first, t.second))
        }
        currentCardType.postValue(t.third)
    }

    fun updateRandomTaskCard(t: TimedTask){
        timedTaskCard.value = t
    }

}

private fun MutableLiveData<Int>.postEmpty() {
    this.postValue(0)
}