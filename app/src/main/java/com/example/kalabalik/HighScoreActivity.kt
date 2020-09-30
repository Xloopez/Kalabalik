package com.example.kalabalik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HighScoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        startActivity(Intent(this, HighScoreActivity::class.java))

        /*//Letar efter vår recyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        //Skapar vår adapter
        val adapter = HighScoreViewAdapter(this, GameSettings.listOfPlayers)

        //Kopplar recycklen med adaptern
        recyclerView.adapter = adapter*/

    }
}