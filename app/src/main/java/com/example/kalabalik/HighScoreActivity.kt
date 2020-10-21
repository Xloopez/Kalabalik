package com.example.kalabalik

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_high_score.*

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

        replayBtn = findViewById(R.id.playAgainButton)

        replayBtn.setOnClickListener {
            playAgain(this)
        }


    }

    override fun onResume() {
        super.onResume()

        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun playAgain(context: Activity) {
        /*val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            (context as Activity).finish()
        }
        Runtime.getRuntime().exit(0)*/


        val intent = Intent(this, MainActivity::class.java) //change it to your main class

        //the following 2 tags are for clearing the backStack and start fresh
        //the following 2 tags are for clearing the backStack and start fresh
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        finishAffinity()
        startActivity(intent)


    }



}