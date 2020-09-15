package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.myapplication.databinding.ActivityGamingBinding

class GamingActivity : AppCompatActivity() {

    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvGamingInfo: AppCompatTextView

    private lateinit var rgPlayers: RadioGroup

    private lateinit var btnSuccess: AppCompatButton

    private val testList0 by lazy { resources.getStringArray(R.array.ConsequencesOrBattle) }
    private val testList1 by lazy { resources.getStringArray(R.array.Consequences) }
    private val testList2 by lazy { resources.getStringArray(R.array.Battle) }

    private var pCount = GameSettings.playerCount
    private var currRound = 1
    private var currTurn = 1
    private var totalRounds = GameSettings.amountOfRounds
    private lateinit var currPlayer: Player

    private lateinit var binding: ActivityGamingBinding

    private var newTitle: String = ""

    //TODO Button Cancel before done? Show final score?
    //TODO Override backbutton?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Game started"
        setUpViews()

        newTitle = getString(R.string.now_playing_round, currRound, totalRounds)
        tvTitle.apply {
            text = newTitle
        }
        tvGamingInfo.apply {
            text = randomizeCard()
        }

        addPlayersToRadioGroup()
        activatePlayerRadioBtn(currTurn)

        btnSuccess.setOnClickListener {

            when(currRound){
                in 1..totalRounds -> {

                    newTitle = getString(R.string.now_playing_round, currRound, totalRounds)
                    tvTitle.text = newTitle
                    nextPlayerTurn()
                    val newCard = randomizeCard() // new Card
                    tvGamingInfo.text = newCard // new Card
                    currPlayer = GameSettings.getPlayerNameByNum(currTurn - 1)
                    activatePlayerRadioBtn(playerNum = currPlayer.playerNum)
                    Log.d("!", "Round $currRound - Turn $currTurn - Player ${currPlayer.playerNum} ")

                    /* CHECK LAST ROUND REACHED AND PLAYER 1 HAS STARTED (move check first) */

                    isFinalRound()

                    when (currTurn == pCount) {
                        true -> {
                            currTurn = 0
                            nextRound()
//                          newTitle = "Now playing round $currRound out of 15"

                        }
                    }
                }
                totalRounds.plus(1) -> {
                    //TODO start next activity
                    Log.d("!", "GAME ENDED")
                    btnSuccess.visibility = View.GONE
                }
            }

        }
    }

    private fun isFinalRound() {
        when (currRound) {
            totalRounds -> {
                when (currPlayer.playerNum) {
                    1 -> {
                        Log.d("!", "FINAL ROUND")
                    }
                }
            }
        }
    }

//    private fun lastRoundAndPlayerOne(cPlayer: Player): Boolean {
//        when (currRound) {
//            totalRounds -> {
//                when (cPlayer.playerNum) {
//                    1 -> {
//                        return true
//                    }
//                }
//            }
//        }
//        return false
//    }

//    private fun endGame(): Boolean = when (currRound) {
//         -> true
//        else -> false
//    }

    private fun setUpViews(){

        binding.apply {
            /* TEXTVIEWS */
            tvTitle = textViewTitle
            tvGamingInfo = textViewGamingInfo

            /* RADIOGROUPS */
            rgPlayers = radioGroupPlayers

            /* BUTTONS */
            btnSuccess = buttonSuccess
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
        //Log.d("!", "Pnum $playerNum")
        val rdBtn = rgPlayers.findViewWithTag<AppCompatRadioButton>(playerNum)
        rdBtn.isChecked = true
    }

    private fun nextPlayerTurn(){
        currTurn++
    }

    private fun nextRound(){
        currRound++
    }

    private fun randomizeCard() = when (testList0.random()) {
        "Consequences" ->  testList1.random()
        "Battle" ->  testList2.random()
        else -> ""
    }

}
