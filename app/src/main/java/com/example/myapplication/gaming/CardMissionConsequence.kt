package com.example.myapplication.gaming

import com.example.myapplication.utilities.EnumUtil

data class CardMissionConsequence
    (
    var listStr: String,
    var points: Double,
    var type: EnumUtil.EnRandom,
) {
    fun getTypeString(): String {
        return type.getEnumString()
    }
}