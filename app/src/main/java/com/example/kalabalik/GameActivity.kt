package com.example.kalabalik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_player.*
import kotlin.math.round

class GameActivity : AppCompatActivity() {

    lateinit var displayPlayerName: TextView
    lateinit var btnNext: Button
    var counter = 0
    var amountOfRounds = GameSettings.amountOfRounds
    var currentRound = 1
    val  name = GameSettings.listOfPlayers.get(counter)//.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Animator object


        btnNext = findViewById(R.id.btnNextPlayerTurn)
        displayPlayerName = findViewById(R.id.playerNameTurn)
        //displayPlayerName.setText("$name: s tur!")

        btnNext.setOnClickListener{
            playerTurn()
        }
    }

    fun playerTurn() {
        val name = GameSettings.listOfPlayers.get(counter)//.text.toString()

        when (currentRound) {
            1 -> {
                Log.d("!!!", "Round $currentRound")
                increaseRounds()
            }
            in 2 until amountOfRounds -> {
                //Log.d("!!!", "Round $currentRound")
                increaseCounterByOne()
                displayPlayerName.setText("$name:s tur!")
                //increaseCounterByOne()

                if (counter == GameSettings.playerCount) {
                    Log.d("!!!", "Round $currentRound")
                    increaseRounds()
                    restartcounter()
                }
                
                when (currentRound) {
                    amountOfRounds -> {
                        Log.d("!!!", "Last Round!")
                        displayPlayerName.setText("$name:s tur!")
                        increaseCounterByOne()
                    }
                }
            }
        }
    }

    fun increaseCounterByOne(){
        counter++
    }
    fun increaseRounds(){
        currentRound++
    }
    fun restartcounter(){
        counter = 0
    }
    fun rightClick(points: Int){

    }
}
