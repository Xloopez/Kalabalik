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
    //var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //playerAmount = findViewById(R.id.amountOfPlayers)
        buttonNext = findViewById(R.id.buttonNext)


        buttonNext.setOnClickListener{
            buttonNextPage()
        }
    }
    fun buttonNextPage(){
        playerAmount = findViewById(R.id.amountOfPlayers)
        GameSettings.playerCount = playerAmount.text.toString().toInt()

        //När next button klickas kommer vi till nästa vy
        val intent = Intent(this, PlayerActivity::class.java)
        //intent.putExtra("amount", amount)
        startActivity(intent)
    }

    /*override fun onClick(v: View?) {

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
    }*/
}