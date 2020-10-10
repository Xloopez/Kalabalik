package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SharedViewModel: ViewModel() {

    var listOfPlayers = mutableListOf<Player>()
    var lopSortedByPoints = mutableListOf<Player>()
    var listOfRandomTimedTaskTurns = mutableListOf<Int>()

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
        //TODO SHUFFLE TAKE PLAYERCOUNT, SPREAD OUT VALUES CANT BE TO CLOSE
        listOfRandomTimedTaskTurns = 6.rangeTo(playerCount.value!!.times(amountOfRounds.value!!))
            .filterNot { i: Int -> (i % playerCount.value!!.plus(1) == 0)}
            .shuffled()
            .distinctBy { 6 }
            .take(playerCount.value!!)
            .toMutableList()
    }                                       

}

private fun MutableLiveData<Int>.increaseBy(i: Int) {
    this.postValue(this.value?.plus(i))
}
private fun MutableLiveData<Int>.putValue(i: Int) {
    this.value = i
}

