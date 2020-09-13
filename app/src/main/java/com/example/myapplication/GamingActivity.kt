package com.example.myapplication

import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView

class GamingActivity : AppCompatActivity() {

    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvGamingInfo: AppCompatTextView

    private lateinit var rgPlayers: RadioGroup

    private lateinit var btnSuccess: AppCompatButton

    private val testList0 = arrayListOf("Konsekvens", "Kamp")
    private val testList1 = arrayListOf("Konsekvens1","Konsekvens2","Konsekvens3","Konsekvens4","Konsekvens5")
    private val testList2 = arrayListOf("Kamp1","Kamp2","Kamp3","Kamp4","Kamp5")

    private var pCount = GameSettings.playerCount
    private var currRound = 1
    private var currTurn = 1

    private var newTitle: String = ""

    //TODO Button Cancel before done? Show final score?
    //TODO Override backbutton?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaming)
        title = "Game started"

        tvTitle = findViewById(R.id.textView_title)
        tvGamingInfo = findViewById(R.id.textView_gaming_info)

        rgPlayers = findViewById(R.id.radioGroup_players)

        btnSuccess = findViewById(R.id.button_Success)

        newTitle = "Now playing round $currRound out of 15"
        tvTitle.apply {
            text = newTitle
        }
        tvGamingInfo.apply {
            text = randomizeCard()
        }

        addPlayersToRadioGroup()

        btnSuccess.setOnClickListener {
            var cPlayer = GameSettings.getPlayerNameByNum(currTurn)
            //println("${cPlayer?.name.toString()}, ${randomizeCard()}")
            var newCard = randomizeCard()
            tvGamingInfo.text = newCard
            nextPlayerTurn()

            when(currTurn > pCount){
                true ->  {
                    currTurn = 1
                    nextRound()
                    newTitle = "Now playing round $currRound out of 15"
                    tvTitle.text = newTitle
                }
            }
        }

        //startRounds()

    }

    private fun addPlayersToRadioGroup() {

        GameSettings.listOfPlayers.forEach { p ->

            val rb = RadioButton(this)
            rb.apply {
                text = p.name
                isClickable = false
            }
            rgPlayers.addView(rb)

        }

    }

    private fun nextPlayerTurn(){
        currTurn++
    }

    private fun nextRound(){
        currRound++
    }

    private fun randomizeCard() = when (testList0.random()) {
        "Konsekvens" ->  testList1.random()
        "Kamp" ->  testList2.random()
        else -> ""
    }

}
