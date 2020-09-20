package com.example.kalabalik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    lateinit var playerAmount: EditText
    lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerAmount = findViewById(R.id.amountOfPlayers)
        buttonNext = findViewById(R.id.buttonNext)

        buttonNext.setOnClickListener{
            buttonNextPage()
        }
    }
    fun buttonNextPage(){
        GameSettings.playerCount = playerAmount.text.toString().toInt()

        //När next button klickas kommer vi till nästa vy
        val intent = Intent(this, PlayerActivity::class.java)
        startActivity(intent)
    }
}