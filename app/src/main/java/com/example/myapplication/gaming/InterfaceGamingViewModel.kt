package com.example.myapplication.gaming

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.dataclasses.Player

interface InterfaceGamingViewModel {
    var currentCard: MutableLiveData<CardMissionConsequence>
    var timedTaskCard: MutableLiveData<CardTimedTask>
    var updateCardFragment: MutableLiveData<Int>
    var clearCardFragment: MutableLiveData<Int>
    var currentTurn: MutableLiveData<Int>
    var currentRound: MutableLiveData<Int>
    var currentPlayer: MutableLiveData<Player>
    fun updatePlayer(player: Player)
    fun updateCurrentCard(card: CardMissionConsequence)
    fun updateRandomTaskCard(cardTimedTask: CardTimedTask)
}