package com.example.myapplication.dataclasses

import com.example.myapplication.TimedTask
import java.util.*

class TimedTasks {

    private var listOfTasks: MutableList<TimedTask> = mutableListOf()

    //TA BORT SEN? //TODO ADD ARRAYS IN .XML
    private val arrayOfColors = mutableListOf("Blå", "Lila", "Röd")
    private val arrayOfFeel = mutableListOf("Mjuk", "Hård", "Sladdrig")
    private val arrayOfShapes = mutableListOf("Rund", "Rektangulär", "Fyrkantig", "Triangulär")
    private val listOfListTasks: MutableList<MutableList<String>> = mutableListOf(arrayOfColors, arrayOfFeel, arrayOfShapes)

    private val arrayOfCons = mutableListOf("Snäpp på höger öra", "Snäpp på vänster öra", "Klapp på huvudet")

    init {

        //TODO ADD RANDOM SECS BETWEEN 30-60?
        for (i in 1.. listOfListTasks.count().times(arrayOfColors.count())){
            val tt = TimedTask(randomListTask(), randomListCon(), 10L)
            listOfTasks.add(tt)
        }
    }

    fun randomTask(): TimedTask { return listOfTasks.random()}

    private fun randomListTask(): String {
        val randomList = listOfListTasks.getRandomList()
        val randomTask = randomList.getRandom()

        return StringBuilder().apply {
            append("Leta upp en")
            append(" ")
            append(randomTask.toUpperCase(Locale.getDefault()))
            append(" ")
            append("sak och återvänd innan tiden är slut!")
            appendLine()
        }.toString()
    }
    private fun randomListCon(): String {
        val randomCon = arrayOfCons.getRandom()

        return StringBuilder().apply {
            append("Alla spelare får ge en")
            append(" ")
            append(randomCon.toUpperCase(Locale.getDefault()))
            append(" ")
            append("till den som misslyckas")
        }.toString()
    }

}
private fun MutableList<MutableList<String>>.getRandomList(): MutableList<String> { return this.random() }
private fun MutableList<String>.getRandom(): String { return this.random() }