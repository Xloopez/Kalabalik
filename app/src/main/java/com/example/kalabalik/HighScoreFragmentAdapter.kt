package com.example.kalabalik

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class HighScoreFragmentAdapter (
    val context: Context,
    var listOfPlayersHighscore: List<Item> = listOf()
) : RecyclerView.Adapter<HighScoreFragmentAdapter.ViewHolder>()
{

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.high_score_list_item, parent, false)

        highestScoreSorted()
        //GameSettings.highestScoreSorted()

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        //Reurnerar vårt demand till recycklerViewn
        return listOfPlayersHighscore.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
        //Klass som innehåller vår view och håller reda på dem i variabler.
        val scoreOfPlayer = itemView.findViewById<TextView>(R.id.scoreOfPlayer)
        val nameOfPlayer = itemView.findViewById<TextView>(R.id.nameOfPlayer)


    }
    fun highestScoreSorted(){
        var swap = true

        while(swap){
            swap = false

            for( i in 1 until listOfPlayersHighscore.size-1) {
                if (listOfPlayersHighscore[i].score < listOfPlayersHighscore[i+1].score){
                    var temp = listOfPlayersHighscore[i].score
                    listOfPlayersHighscore[i].score = listOfPlayersHighscore[i+1].score
                    listOfPlayersHighscore[i+1].score = temp

                    swap = true
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Tar rätt person från vår lista
        val name = listOfPlayersHighscore[position]
        holder.scoreOfPlayer.text = name.score.toString()
        holder.nameOfPlayer.text = name.name
    }
}