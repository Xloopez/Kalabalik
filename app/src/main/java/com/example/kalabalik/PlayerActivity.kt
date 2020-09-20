package com.example.kalabalik

import android.content.Intent
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
    lateinit var buttonAddName: Button
    var counter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerName = findViewById(R.id.personName1)
        buttonAddName = findViewById(R.id.buttonNextName)

        buttonAddName.setOnClickListener{
            onClick()
        }

    }

    fun onClick() {
        val  name = playerName.text.toString()

        when (counter){
                in 1 until GameSettings.playerCount -> {
                    GameSettings.addPlayerToList(name)
                    increaseCounterByOne()
                    playerName.text.clear()
                    playerName.setHint("Spelare $counter")
                    when (counter){
                        GameSettings.playerCount -> buttonNextName.setText(R.string.button_start_game)  //buttonNextName.setText("Starta spelet")
                    }
                }
                GameSettings.playerCount -> {
                    GameSettings.addPlayerToList(name)
                    increaseCounterByOne()
                    val intent = Intent(this, GameActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    fun increaseCounterByOne(){
        counter++
    }



}