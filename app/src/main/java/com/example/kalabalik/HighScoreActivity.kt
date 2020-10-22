package com.example.kalabalik

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_high_score.*


class HighScoreActivity : AppCompatActivity() {

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

        replayBtn = findViewById(R.id.playAgainButton)

        replayBtn.setOnClickListener {
            playAgain(this)
        }


    }

    override fun onResume() {
        super.onResume()

        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun playAgain(context: Context) {
        startActivity(Intent(this, MainActivity::class.java))
        this.finishActivity(0)
    }



}