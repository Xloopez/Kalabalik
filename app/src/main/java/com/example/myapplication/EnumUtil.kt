package com.example.myapplication

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