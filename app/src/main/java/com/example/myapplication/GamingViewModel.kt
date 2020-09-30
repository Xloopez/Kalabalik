package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    fun updateCardTypeAndPair(pair: Pair<String, Double>, cardType: EnumUtil.EnRandom){
        when (cardType) {
            EnumUtil.EnRandom.CONSEQUENCES -> consequencePair.postValue(pair)
            EnumUtil.EnRandom.MISSION -> missionPair.postValue(pair)
        }
        currentCardType.postValue(cardType)
    }

}

private fun MutableLiveData<Int>.postEmpty() {
    this.postValue(0)
}