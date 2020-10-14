package com.example.myapplication.dataclasses

import com.example.myapplication.gaming.CardMissionConsequence

data class Player
    (
    var name: String,
    var listOfCards: MutableList<CardMissionConsequence> = mutableListOf(),
    var playerNum: Int,
    )
{

    fun listAddRoundAndPoints(updatedCard: CardMissionConsequence) {
        listOfCards.add(updatedCard)
    }

    fun getCardsList(): MutableList<CardMissionConsequence> {
        return listOfCards
    }

    fun sumPointsFromListCards(): Double {
        return listOfCards.sumByDouble { card -> card.points }
    }

}