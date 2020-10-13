package com.example.myapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dataclasses.Player
import com.example.myapplication.gaming.CardMissionConsequence
import com.example.myapplication.gaming.GeneratorMissionOrConsequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    var listOfPlayers = mutableListOf<Player>()
    var lopSortedByPoints = mutableListOf<Player>()
    var listOfRandomTimedTaskTurns = mutableListOf<Int>()
    var listOfMissionOrConsequenceTurns = mutableListOf<Pair<Int, CardMissionConsequence>>()
    private val evenTurns: Int get() = pCount.plus(1)
    val amRounds: Int get() = amountOfRounds.value!!
    val pCount: Int get() = playerCount.value!!
    val totalTurnsPlus: Int get() = amRounds.times(pCount).plus(amRounds)

    var currentFragmentPos = MutableLiveData<Int>().apply {
        value = 0
    }

    var playerCount = MutableLiveData<Int>().apply {
        value = 0
    }

    var amountOfRounds = MutableLiveData<Int>().apply {
        value = 4
    }

    fun updateFragmentPos(){
        currentFragmentPos.increaseBy(1)
    }

    fun setPlayerCount(i: Int) = viewModelScope.launch {
        playerCount.putValue(i)
    }

    fun addPlayerToList(player: Player) = viewModelScope.launch(Dispatchers.IO) {
        listOfPlayers.add(player)
    }

    fun listSumPairSort() = viewModelScope.launch(Dispatchers.IO) {
        lopSortedByPoints = listOfPlayers
            .sortedByDescending { player -> player.sumPointsFromListPair() }
            .toMutableList()
    }

    fun createListOfMissionConsequence() = viewModelScope.launch {
        for (i in ((1..totalTurnsPlus).filter { (it % evenTurns) != 0 })) {
            listOfMissionOrConsequenceTurns.add(
                Pair(
                    i,
                    GeneratorMissionOrConsequence(context).generateNewCard()
                )
            )
        }
    }

    fun getListCard(currentTurn: Int): CardMissionConsequence {
        Log.d("!", "getListCard() $currentTurn")
        return listOfMissionOrConsequenceTurns.find { it.first == currentTurn }!!.second
    }

    fun createRandomTaskList() = viewModelScope.launch {

        var ran: Int
        val random = Random
        val listTasksTurns: MutableList<Int> = mutableListOf()
        val listOfExcludeValues: MutableList<Int> = mutableListOf()

        listOfExcludeValues.addExcludeEvenTurns(totalTurnsPlus, evenTurns)

        do {
            ran = random.nextInt(evenTurns, totalTurnsPlus)
            val ranExcludeInterval = 1.createTaskExcludeTurns(ran)

            if (!listOfExcludeValues.contains(ran)) {
                listTasksTurns.add(ran)
                listOfExcludeValues.addAll(ranExcludeInterval)
            }

        }while (listTasksTurns.count() != pCount)

        listOfRandomTimedTaskTurns.addAll(listTasksTurns)
    }

    private fun Int.createTaskExcludeTurns(excludeRef: Int): MutableList<Int> {
        return (excludeRef.minus(this)..excludeRef.plus(this)).toMutableList()
    }

    private fun MutableList<Int>.addExcludeEvenTurns(totalTurns: Int, evenTurns: Int) {
        (1..totalTurns).filter { (it % evenTurns) == 0 }.run { addAll(this) }
    }

}

private fun MutableLiveData<Int>.increaseBy(i: Int) {
    this.postValue(this.value?.plus(i))
}
private fun MutableLiveData<Int>.putValue(i: Int) {
    this.value = i
}



