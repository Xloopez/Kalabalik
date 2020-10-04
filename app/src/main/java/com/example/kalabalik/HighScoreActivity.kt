package com.example.kalabalik

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_high_score.*
import kotlin.system.exitProcess

class HighScoreActivity : AppCompatActivity() {

    lateinit var cancelGamebtn: Button
    lateinit var replayBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        //Letar efter vår recyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        //Skapar vår adapter
        val adapter = HighScoreViewAdapter(this, GameSettings.listOfPlayers)

        //Kopplar recycklen med adaptern
        recyclerView.adapter = adapter


        cancelGamebtn = findViewById(R.id.finnishButton)
        replayBtn = findViewById(R.id.playAgainButton)

        cancelGamebtn.setOnClickListener {
            cancelGame()
        }
        replayBtn.setOnClickListener {
            playAgain(this)
        }


    }

    override fun onResume() {
        super.onResume()

        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun cancelGame(){
        finishAffinity()
    }

    fun playAgain(context: Activity) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            (context as Activity).finish()
        }
        Runtime.getRuntime().exit(0)
    }



}