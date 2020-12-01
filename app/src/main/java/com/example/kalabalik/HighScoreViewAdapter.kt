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

class HighScoreViewAdapter (
    val context: Context,
    val listOfPlayer: MutableList<Player> = mutableListOf()
) : RecyclerView.Adapter<HighScoreViewAdapter.ViewHolder>()
{

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.high_score_list_item, parent, false)

        GameSettings.highestScoreSorted(listOfPlayer)
        //GameSettings.highestScoreSorted()

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Tar rätt person från vår lista
        val name = listOfPlayer[position]
        holder.scoreOfPlayer.text = name.points.toString()
        holder.nameOfPlayer.text = name.name
    }

    override fun getItemCount(): Int {
        //Reurnerar vårt demand till recycklerViewn
        return listOfPlayer.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
        //Klass som innehåller vår view och håller reda på dem i variabler.
        val scoreOfPlayer = itemView.findViewById<TextView>(R.id.scoreOfPlayer)
        val nameOfPlayer = itemView.findViewById<TextView>(R.id.nameOfPlayer)


    }
    /*fun highestScoreSorted(){
        var swap = true

        while(swap){
            swap = false

            for( i in 0 until listOfPlayer.size-1) {
                if (listOfPlayer[i].points < listOfPlayer[i+1].points){
                    var temp = listOfPlayer[i].points
                    listOfPlayer[i].points = listOfPlayer[i+1].points
                    listOfPlayer[i+1].points = temp

                    swap = true
                }
            }
        }
    }*/
}