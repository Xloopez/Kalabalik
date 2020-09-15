package com.example.kalabalik

import android.location.GnssMeasurement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {
    lateinit var playerAmount: EditText
    lateinit var playerName: EditText
    lateinit var buttonStartG: Button
    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerAmount = findViewById(R.id.personName1)
        buttonStartG = findViewById(R.id.buttonStartGame)
        buttonStartG.setOnClickListener{
        }

    }



    override fun onClick(v: View?) {

        when(v?.id) {
            R.id.buttonNext -> {

                when (counter) {
                    0 -> {
                        GameSettings.playerCount = Integer.parseInt(playerAmount.text.toString())
                        increaseCounterByOne()
                    }
                    in 1 until GameSettings.playerCount -> {
                        var playerNames = findViewById<>(R.)

                    }



                }
            }
        }
    }
    fun increaseCounterByOne(){
        counter++
    }
}