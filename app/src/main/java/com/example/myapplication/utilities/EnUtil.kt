package com.example.myapplication.utilities

import com.example.myapplication.R

enum class EnRandom {
    CONSEQUENCES,
    MISSION;

    fun getEnumString(): Int = when (this) {
        CONSEQUENCES -> R.string.consequence
        MISSION -> R.string.mission
    }

    fun getBackGroundColor(): Int = when (this) {
        CONSEQUENCES -> R.color.deep_purple_200
        MISSION -> R.color.deep_purple_600
    }
}

enum class EnOperation { SUCCESS, FAIL; }
enum class EnScore { MINI, FINAL; }
