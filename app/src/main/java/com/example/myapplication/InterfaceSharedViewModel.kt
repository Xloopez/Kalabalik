package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.dataclasses.Player
import com.example.myapplication.gaming.CardMissionConsequence
import kotlinx.coroutines.Job

interface ISharedViewModel {
    var listOfPlayers: MutableList<Player>
    var lopSortedByPoints: MutableList<Player>
    var listOfRandomTimedTaskTurns: MutableList<Int>
    var listOfMissionOrConsequenceTurns: MutableList<Pair<Int, CardMissionConsequence>>
    val amRounds: Int
    val pCount: Int
    val totalTurnsPlus: Int
    var currentFragmentPos: MutableLiveData<Int>
    var playerCount: MutableLiveData<Int>
    var amountOfRounds: MutableLiveData<Int>
    fun updateFragmentPos()
    fun setPlayerCount(i: Int): Job
    fun getListCard(currentTurn: Int): CardMissionConsequence
    fun addPlayerToList(player: Player): Job
    fun listSumPairSort(): Job
    fun createListOfMissionConsequence(): Job
    fun createRandomTaskList(): Job
}