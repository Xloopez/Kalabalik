package com.example.myapplication.gaming

import com.example.myapplication.EnRandom

data class CardMissionConsequence
    (
    var listStr: String,
    var points: Double,
    var type: EnRandom,
    private var round: Int? = null,
) {
    fun getType(): Int {
        return type.getEnumString()
    }

    fun getBackColor(): Int {
        return type.getBackGroundColor()
    }

    fun getRound(): Int? {
        return round
    }

    fun setRound(int: Int) {
        round = int
    }

}
