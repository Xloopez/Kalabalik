package com.example.myapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.dataclasses.Card
import com.example.myapplication.dataclasses.Player
import com.example.myapplication.dataclasses.TimedTask

class GamingViewModel : ViewModel() {

    var currentCard = MutableLiveData<Card>()
    var timedTaskCard = MutableLiveData<TimedTask>()

    var updateCardFragment = MutableLiveData<Int>()
    var clearCardFragment = MutableLiveData<Int>()

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

    fun updateCurrentCard(card: Card){
        currentCard.postValue(card)
    }

    fun updateRandomTaskCard(t: TimedTask){
        timedTaskCard.value = t
    }

}

private fun MutableLiveData<Int>.postEmpty() {
    this.postValue(0)
}