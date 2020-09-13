package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView

class GamingActivity : AppCompatActivity() {

    lateinit var tvTitle: AppCompatTextView
    lateinit var tvGamingInfo: AppCompatTextView

    lateinit var rgPlayers: RadioGroup

    //TODO Button Cancel before done? Show final score?
    //TODO Override backbutton?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaming)
        title = "Game started"

        tvTitle = findViewById(R.id.textView_title)
        tvGamingInfo = findViewById(R.id.textView_gaming_info)

        rgPlayers = findViewById(R.id.radioGroup_players)

        tvTitle.apply {
            text = "Now playing round .. out of 15"
        }
        tvGamingInfo.apply {
            text = "You have been dealt this card"
        }

        addPlayersToRadioGroup()
        startRounds()

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

    private fun startRounds() {

        val testList0 = arrayListOf("Konsekvens", "Kamp")
        val testList1 = arrayListOf("Quest1","Quest2","Quest3","Quest4","Quest5")
        val testList2 = arrayListOf("Kamp1","Kamp2","Kamp3","Kamp4","Kamp5")

        val roundsToPlay = GameSettings.amountOfRounds
        val totalRounds = (roundsToPlay.times(GameSettings.listOfPlayers.count()))

        for (i in 1..totalRounds) {

            when (testList0.random()) {
                "Konsekvens" -> {
                    Log.d("!", testList1.random())
                }
                "Kamp" -> {
                    Log.d("!", testList2.random())
                }

            }

        }


//                for (p in GameSettings.listOfPlayers) {
//                    for (rb in rgPlayers.iterator()){
//                        val id = rb.id
//                        val rbId = rgPlayers.findViewById<RadioButton>(id)
//                        when(rbId.text == p.name){
//                            true -> {
//                                rbId.isChecked = true
//                            }
//                        }
//                    }
//                    Log.d("A","Now playing round $i out of 15, ${p.name}")
//                }
//
//                tvTitle.let {
//                    it.text = "Now playing round $i out of 15"
//                }

//        }
    }
}