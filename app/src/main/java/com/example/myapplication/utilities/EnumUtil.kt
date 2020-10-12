package com.example.myapplication.utilities

class EnumUtil {

    enum class EnRandom {
        CONSEQUENCES,
        MISSION;

        fun getEnumString(): String = when(this) {
            CONSEQUENCES -> "CONSEQUENCE"
            MISSION -> "MISSION"
        }
    }
    enum class EnOperation { SUCCESS, FAIL; }

}