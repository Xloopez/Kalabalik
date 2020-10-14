package com.example.myapplication.gaming

import com.example.myapplication.utilities.EnumUtil

data class CardMissionConsequence
    (
    var listStr: String,
    var points: Double,
    var type: EnumUtil.EnRandom,
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
