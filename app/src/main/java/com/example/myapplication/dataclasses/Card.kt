package com.example.myapplication.dataclasses

import com.example.myapplication.utilities.EnumUtil

data class Card
    (
    var listStr: String,
    var points: Double,
    var type: EnumUtil.EnRandom,
)
{
    fun getTypeString(): String {
        return type.getEnumString()
    }
}