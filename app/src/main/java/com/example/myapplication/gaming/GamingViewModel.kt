package com.example.myapplication.gaming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.dataclasses.Player

class GamingViewModel : ViewModel(), InterfaceGamingViewModel {

    override var currentCard = MutableLiveData<CardMissionConsequence>()
    override var timedTaskCard = MutableLiveData<CardTimedTask>()
    override var updateCardFragment = MutableLiveData<Int>()
    override var clearCardFragment = MutableLiveData<Int>()

    override var currentTurn = MutableLiveData<Int>().apply {
        value = 0
    }
    override var currentRound = MutableLiveData<Int>().apply {
        value = 0
    }

    override var currentPlayer = MutableLiveData<Player>().apply {
        value = Player("InitialPlayer0", playerNum = 0)
    }

    override fun updatePlayer(player: Player) = currentPlayer.postValue(player)

    override fun updateCurrentCard(card: CardMissionConsequence) {
        currentCard.postValue(card)
    }

    override fun updateRandomTaskCard(cardTimedTask: CardTimedTask) {
        timedTaskCard.value = cardTimedTask
    }

}

fun MutableLiveData<Int>.postEmpty() {
    this.postValue(0)
}

fun MutableLiveData<Int>.postUpdateIntBy(int: Int) {
    this.postValue(this.value?.plus(int))
}