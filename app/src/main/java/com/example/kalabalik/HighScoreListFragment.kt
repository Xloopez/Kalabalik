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

class HighScoreListFragment: Fragment() {

    lateinit var highScoreRecyclerView: RecyclerView

    //lateinit var list: MutableList<Player>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*highScoreRecyclerView = view?.findViewById(R.id.fragmentRecyclerView)!!

        job = Job()

       // db = Room.databaseBuilder(getActivity()?.applicationContext!!, AppDatabase::class.java, "high-score-list")
       //     .fallbackToDestructiveMigration().build()

        dataB = getActivity()?.applicationContext?.let {
            Room.databaseBuilder(it, AppDatabase::class.java, "high-score-list")
                .fallbackToDestructiveMigration().build()
        }!!


        //Letar efter min highscore-recyclerView
        highScoreRecyclerView = view?.findViewById<RecyclerView>(R.id.fragmentRecyclerView)!!
        highScoreRecyclerView?.layoutManager = LinearLayoutManager(context)

        //Skapar vår adapter
        val adapter = context?.let { HighScoreViewAdapter(it, GameSettings.listOfPlayers) }

        //Kopplar recyclerViewn med adaptern
        highScoreRecyclerView?.adapter = adapter

        //val player1 = Player("Nicole", 10)
        //GameSettings.listOfPlayers.add(player1)

        //val itemPlayer = Item(0, GameSettings.listOfPlayers[0].name, GameSettings.listOfPlayers[0].points)
        //saveItem(itemPlayer)*/

        //Letar efter min highscore-recyclerView

        return inflater.inflate(R.layout.fragment_high_score_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        highScoreRecyclerView = view?.findViewById(R.id.fragmentRecyclerView)!!


        //Letar efter min highscore-recyclerView
        highScoreRecyclerView = view?.findViewById<RecyclerView>(R.id.fragmentRecyclerView)!!
        highScoreRecyclerView?.layoutManager = LinearLayoutManager(context)

        //Skapar vår adapter
        val adapter = context?.let { HighScoreFragmentAdapter(it, GameSettings.listOfPlayersHighscore) }

        //Kopplar recyclerViewn med adaptern
        highScoreRecyclerView?.adapter = adapter

        //val player1 = Player("Nicole", 10)
        //GameSettings.listOfPlayers.add(player1)


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        highScoreRecyclerView.adapter?.notifyDataSetChanged()
    }
}