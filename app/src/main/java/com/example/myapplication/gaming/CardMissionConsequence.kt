package com.example.myapplication.gaming

import com.example.myapplication.EnRandom

data class CardMissionConsequence
    (
    var listStr: String,
    var points: Double,
    var type: EnRandom,
    private var round: Int? = null,
) {
    fun getTypeString(): String {
        return type.getEnumString()
    }

    fun getRound(): Int? {
        return round
    }

    fun setRound(int: Int){
        round = int
    }


}
