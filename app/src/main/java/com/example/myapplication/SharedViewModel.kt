package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SharedViewModel: ViewModel() {

    var listOfPlayers = mutableListOf<Player>()
    var lopSortedByPoints = mutableListOf<Player>()

    var currentFragmentPos= MutableLiveData<Int>().apply {
        value = 0
    }

    var playerCount = MutableLiveData<Int>().apply {
        value = 0
    }
    var amountOfRounds = MutableLiveData<Int>().apply {
        value = 3
    }

    fun addPlayerToList(player: Player){
        listOfPlayers.add(player)
    }

    fun sorted() = viewModelScope.launch {
         lopSortedByPoints = listOfPlayers.sortedByDescending { player -> player.sumPointsFromListPair()}.toMutableList()
    }


    fun getPlayerFinalResultSorted(): MutableList<Player>
            = listOfPlayers.sortedByDescending { player -> player.sumPointsFromListPair()}.toMutableList()



}