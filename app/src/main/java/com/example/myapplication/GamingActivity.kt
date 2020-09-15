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
    private val testList2  by lazy { resources.getStringArray(R.array.Battle) }

    private var pCount = GameSettings.playerCount
    private var currRound = 1
    private var currTurn = 1
    private var totalRounds = GameSettings.amountOfRounds

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

            when(endGame()){
                true -> {
                   //TODO start next activity
                    Log.d("!", "GAME ENDED")
                    btnSuccess.visibility = View.GONE
                }
                false -> {

                    nextPlayerTurn()

                    val cPlayer = GameSettings.getPlayerNameByNum(currTurn)
                    activatePlayerRadioBtn(playerNum = cPlayer!!.playerNum)

                    when(currRound){
                        in 1..totalRounds -> {

                            Log.d("!", "Round $currRound - Turn $currTurn - Player ${cPlayer.name}")

                            /* CHECK LAST ROUND REACHED AND PLAYER 1 HAS STARTED */
                            when(lastRoundAndPlayerOne(cPlayer = cPlayer)){
                                true -> {
                                     //TODO DISPLAY FINAL ROUND STRING AND ANIMATE
                                     Log.d("!", "FINAL ROUND")
                                }
                            }

                            when(currTurn == pCount){
                                true ->  {
                                    currTurn = 0
                                    nextRound()
//                                  newTitle = "Now playing round $currRound out of 15"
                                    newTitle = getString(R.string.now_playing_round, currRound, totalRounds)
                                    tvTitle.text = newTitle
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun lastRoundAndPlayerOne(cPlayer: Player): Boolean {
        when (currRound) {
            totalRounds -> {
                when (cPlayer.playerNum) {
                    1 -> {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun endGame(): Boolean = when (currRound) {
        totalRounds.plus(1) -> true
        else -> false
    }

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
        ///ª
    }

    private fun nextPlayerTurn(){
        currTurn++
        val newCard = randomizeCard()
        tvGamingInfo.text = newCard
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
