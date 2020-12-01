package com.example.kalabalik

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.android.synthetic.main.activity_high_score.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HighScoreListFragment: Fragment(){

    var highScoreRecyclerView: RecyclerView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_high_score_list, container, false)
    }

    fun recyclerView () {
        highScoreRecyclerView = view?.findViewById(R.id.fragmentRecyclerView)!!


        //Letar efter min highscore-recyclerView
        highScoreRecyclerView = view?.findViewById<RecyclerView>(R.id.fragmentRecyclerView)!!
        highScoreRecyclerView?.layoutManager = LinearLayoutManager(context)

        //Skapar vår adapter
        val adapter = context?.let { HighScoreViewAdapter(it, GameSettings.listOfPlayersHighscore) }

        //Kopplar recyclerViewn med adaptern
        highScoreRecyclerView?.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        highScoreRecyclerView?.adapter?.notifyDataSetChanged()
    }
}