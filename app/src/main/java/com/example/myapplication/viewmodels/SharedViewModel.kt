package com.example.myapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dataclasses.Player
import kotlinx.coroutines.launch
import kotlin.random.Random

class SharedViewModel: ViewModel() {

    var listOfPlayers = mutableListOf<Player>()
    var lopSortedByPoints = mutableListOf<Player>()
    var listOfRandomTimedTaskTurns = mutableListOf<Int>()
    val evenTurns: Int get() = calcEvenTurns()
    val amRounds: Int get() = getAmountOfRounds()
    val pCount: Int get() = getPlayerCount()
    val totalTurnsPlus: Int get() = calcTotalTurnsPlus()

    var currentFragmentPos= MutableLiveData<Int>().apply {
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

    fun addPlayerToList(player: Player){
        listOfPlayers.add(player)
    }

    fun listSumPairSorted() = viewModelScope.launch {
         lopSortedByPoints = listOfPlayers
             .sortedByDescending { player -> player.sumPointsFromListPair()}
             .toMutableList()
    }

    fun updateRandomTaskList() = viewModelScope.launch {

        var ran: Int
        val random = Random
        val evenTurns = evenTurns
        val totalTurns = totalTurnsPlus
        val listTasksTurns: MutableList<Int> = mutableListOf()
        val listOfExcludeValues: MutableList<Int> = mutableListOf()
        listOfExcludeValues.addExcludeEvenTurns(totalTurns, evenTurns)

        do {
            ran = random.nextInt(evenTurns, totalTurns)
            val ranExcludeInterval = 1.createExcludeRange(ran)

            if (!listOfExcludeValues.contains(ran)) {
                listTasksTurns.add(ran)
                listOfExcludeValues.addAll(ranExcludeInterval)
            }

        }while (listTasksTurns.count() != pCount)
        listOfRandomTimedTaskTurns.addAll(listTasksTurns)
    }

    private fun getPlayerCount(): Int = playerCount.value!!
    private fun getAmountOfRounds(): Int = amountOfRounds.value!!
    private fun calcEvenTurns(): Int = pCount.plus(1)
    private fun calcTotalTurnsPlus(): Int = amRounds.times(pCount).plus(amRounds)

    private fun Int.createExcludeRange(excludeRef: Int): MutableList<Int> {
        return (excludeRef.minus(this)..excludeRef.plus(this)).toMutableList()
    }

    private fun MutableList<Int>.addExcludeEvenTurns(
        totalTurns: Int,
        evenTurns: Int
    ) {
        (1..totalTurns).filter { (it % evenTurns) == 0 }.run { addAll(this) }
    }

}

private fun MutableLiveData<Int>.increaseBy(i: Int) {
    this.postValue(this.value?.plus(i))
}
private fun MutableLiveData<Int>.putValue(i: Int) {
    this.value = i
}



