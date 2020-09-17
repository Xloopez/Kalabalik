package com.example.kalabalik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_player.*

class GameActivity : AppCompatActivity() {

    lateinit var displayPlayerName: TextView
    lateinit var btnNext: Button
    var counter = 1
    val  name = GameSettings.listOfPlayers.get(0)//.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        displayPlayerName = findViewById(R.id.playerNameTurn)
        displayPlayerName.setText("$name: s tur!")

        btnNext = findViewById(R.id.btnNextPlayerTurn)

        btnNext.setOnClickListener{
            playerTurn()
        }



    }

    fun playerTurn(){
       val  name = GameSettings.listOfPlayers.get(counter)//.text.toString()
        when (counter){
            in 1 until GameSettings.playerCount -> {
                increaseCounterByOne()
                displayPlayerName.setText("$name: s tur!")
            }
            GameSettings.playerCount -> {
                increaseCounterByOne()
                displayPlayerName.setText("$name: s tur!")
            }
        }
    }

    fun increaseCounterByOne(){
        counter++
    }

    fun rightClick(points: Int){

    }
}
