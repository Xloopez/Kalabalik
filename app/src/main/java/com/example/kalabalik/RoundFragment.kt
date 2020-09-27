package com.example.kalabalik

import android.content.Context
import android.os.Bundle
import android.system.Os.remove
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RoundFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //lateinit var recyclerView: RecyclerView
    //lateinit var adapter: HighScoreViewAdapter
    //lateinit var rightButton: Button

    var listOfPlayerName = GameSettings.listOfPlayers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_round, container, false)

        /*
        //leta efter recycklerView
        recyclerView = recyclerView.findViewById(R.id.recyclerView)

        view.recyclerView.layoutManager = LinearLayoutManager(activity)

        //Skapa adapter
        val adapter = HighScoreViewAdapter(this, listOfPlayerName)//HighScoreViewAdapter(context, listOfPlayerName)
        //val adapter: HighScoreViewAdapter = HighScoreViewAdapter()

        //Koppla adapter mer RecyclerView
        view.recyclerView.adapter = adapter*/

        //leta efter recycklerView
        /*recyclerView = recyclerView.findViewById(R.id.recyclerView)
        view?.recyclerView?.layoutManager = LinearLayoutManager(activity)*/



        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        /* //Skapar adaptern
        adapter = HighScoreViewAdapter(context, listOfPlayerName)
        //Kopplar adaptern till recycklerview
        recyclerView.adapter = adapter*/
    }

    fun setContinueButton(){
        //rightButton.text = "Återgå"
    }
}