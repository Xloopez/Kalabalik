package com.example.myapplication.gaming

import android.content.Context
import android.content.res.Resources
import com.example.myapplication.R
import java.util.*

class GeneratorTimedTask(val context: Context) {

    val res: Resources = context.resources

    var listOfTasks: MutableList<CardTimedTask> = mutableListOf()

    private val arrayOfColors = res.getStringArray(R.array.RandomTaskArrayOfColors)
    private val arrayOfFeel = res.getStringArray(R.array.RandomTaskArrayOfFeel)
    private val arrayOfShapes = res.getStringArray(R.array.RandomTaskArrayOfShape)
    private val listOfListTasks: MutableList<Array<String>> =
        arrayListOf(arrayOfColors, arrayOfFeel, arrayOfShapes)
    private val arrayOfCons = res.getStringArray(R.array.RandomTaskArrayOfCons)

    init {
        //TODO ADD RANDOM SECS BETWEEN 30-60? at the least change the timer
        for (i in 1.. listOfListTasks.count().times(arrayOfColors.count())){
            val tt = CardTimedTask(randomListTask(), randomListCon(), 15L)
            listOfTasks.add(tt)
        }
    }

    private fun randomListTask(): String {
        val randomList = listOfListTasks.getRandomList()
        val randomTask = randomList.getRandom()

        return StringBuilder().apply {
            append(context.getString(R.string.find_a))
            append(" ")
            append(randomTask?.toUpperCase(Locale.getDefault()))
            append(" ")
            append(context.getString(R.string.thing_return_before_time_over))
            appendLine()
        }.toString()
    }

    private fun randomListCon(): String {
        val randomCon = arrayOfCons.getRandom()

        return StringBuilder().apply {
            append(context.getString(R.string.all_players_give))
            append(" ")
            append(randomCon?.toUpperCase(Locale.getDefault()))
            append(" ")
            append(context.getString(R.string.to_those_who_fail))
        }.toString()
    }

}

private fun <T> Array<T>.getRandom(): String? { return this.random() as? String }
private fun MutableList<Array<String>>.getRandomList(): Array<String> { return this.random() }
