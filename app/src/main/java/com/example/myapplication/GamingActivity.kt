package com.example.myapplication

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRadioButton
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
    private var totalRounds = GameSettings.amountOfRounds

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

        newTitle = getString(R.string.now_playing_round, currRound, totalRounds)
        tvTitle.apply {
            text = newTitle
        }
        tvGamingInfo.apply {
            text = randomizeCard()
        }

        addPlayersToRadioGroup()
//        activatePlayerRadioBtn(currTurn)

        btnSuccess.setOnClickListener {

            val newCard = randomizeCard()
            tvGamingInfo.text = newCard
            nextPlayerTurn()
            val cPlayer = GameSettings.getPlayerNameByNum(currTurn)
            activatePlayerRadioBtn(playerNum = cPlayer!!.playerNum)

            when(currRound){
                in 1..totalRounds -> {

                    Log.d("!", "Round $currRound - Turn $currTurn")

                    when (currRound) {
                        totalRounds -> {
                            /* MAX ROUNDS REACHED, PLAYER 1 HAS STARTED */
                            when (cPlayer.playerNum) {
                                1 -> {
                                    //TODO DISPLAY FINAL ROUND ANIMATION
                                    Log.d("!", "FINAL ROUND")
                                }
                            }
                        }
                    }

                    when(currTurn == pCount){
                        true ->  {
                            currTurn = 0
                            nextRound()
//                          newTitle = "Now playing round $currRound out of 15"
                            newTitle = getString(R.string.now_playing_round, currRound, totalRounds)
                            tvTitle.text = newTitle
                        }
                    }
                }
                totalRounds.plus(1) -> {
                    //TODO PUT IN OUTER WHEN FUNCTION TO END GAME
                    Log.d("!", "GAME ENDED")
                    btnSuccess.visibility = View.GONE
                }

            }

        }
    }

    private fun addPlayersToRadioGroup() {

        GameSettings.listOfPlayers.forEach { p ->

            val rb = AppCompatRadioButton(this)
            rb.apply {
                text = p.name
                isClickable = false
                tag = p.playerNum
            }
            rgPlayers.addView(rb)

        }

    }

    private fun activatePlayerRadioBtn(playerNum: Int){
        val v = rgPlayers.findViewWithTag<AppCompatRadioButton>(playerNum)
        Log.d("!", "Pnum $playerNum")
        v.isChecked = true
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
