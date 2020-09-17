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

class GamingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvGamingInfo: AppCompatTextView

    private lateinit var rgPlayers: RadioGroup

    private lateinit var btnSuccess: AppCompatButton
    private lateinit var btnFail: AppCompatButton

    private val testList0 by lazy { resources.getStringArray(R.array.ConsequencesOrBattle) }
    private val testList1 by lazy { resources.getStringArray(R.array.Consequences) }
    private val testList2 by lazy { resources.getStringArray(R.array.Battle) }

    private var pCount = GameSettings.playerCount
    private var currRound = 1
    private var currTurn = 0
    private var totalRounds = GameSettings.amountOfRounds
    private lateinit var currPlayer: Player

    private lateinit var binding: ActivityGamingBinding

    private var newTitle: String = ""

    private enum class OPERATION {
        SUCCESS, FAIL;
    }

    //TODO Button Cancel before done? Show final score?
    //TODO Override backbutton?

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityGamingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Game started"
        setUpViews()
        addPlayersToRadioGroup()
        nextPlayerTurn()
        //currPlayer = GameSettings.listOfPlayers[currTurn -1]
        updateTitleView()

        btnSuccess.setOnClickListener(this)
        btnFail.setOnClickListener(this)

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
            btnFail = buttonFail

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
        val rdBtn = rgPlayers.findViewWithTag<AppCompatRadioButton>(playerNum)
        rdBtn.isChecked = true
    }

    private fun nextPlayerTurn(){
        currTurn++
        val newCard = randomizeCard() /* Generate new random card */
        tvGamingInfo.text = newCard /* Update card-info */
        currPlayer = GameSettings.listOfPlayers[currTurn - 1] /* Return(Current Turn - 1) because of ListIndex starts from 0 = 1, 1 = 2 etc.  */
        activatePlayerRadioBtn(playerNum = currPlayer.playerNum)  /* Activate tagged RadioButton by TAG from(currPlayer) */
    }

    private fun updateTitleView(){
      newTitle = getString(R.string.now_playing_round, currRound, totalRounds)
      tvTitle.text = newTitle
    }

    private fun nextRound(){
        currTurn = 0 /* Reset to 0 when new round, btnClick adds 1 directly = Turn 1 on round start */
        currRound++
    }

    private fun randomizeCard() = when (testList0.random()) {
        "Consequences" ->  testList1.random()
        "Battle" ->  testList2.random()
        else -> ""
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_Success -> {
                doNext(OPERATION.SUCCESS)
                //Log.d("!", "SUCCESSFUL")
            }
            R.id.button_Fail -> {
                doNext(OPERATION.FAIL)
                //Log.d("!", "UNSUCCESSFUL")
            }
        }

    }

    private fun doNext(operation: OPERATION) {

        when (operation) {
            OPERATION.SUCCESS -> {
                currPlayer.listOfRoundAndPoints.add(Pair(currRound, 2.0))
            }
            OPERATION.FAIL -> {
                currPlayer.listOfRoundAndPoints.add(Pair(currRound, -1.0))
            }
        }

        when (currTurn == pCount) {
            true -> {
                nextRound()
            }
        }

        when (currRound) {
            in 1..totalRounds -> {

                isFinalRound()
                nextPlayerTurn()
                updateTitleView()

                Log.d("!", "Round $currRound - Turn $currTurn - Player ${currPlayer.playerNum} ")

                /* CHECK LAST ROUND REACHED AND PLAYER 1 HAS STARTED (move check first) */

            }
            totalRounds.plus(1) -> {

                //TODO start next activity
                Log.d("!", "GAME ENDED")

                for(p in GameSettings.listOfPlayers){
                    Log.d("!", "${p.name} ${p.listOfRoundAndPoints}")
                }

                btnSuccess.visibility = View.GONE
                btnFail.visibility = View.GONE
            }
        }
    }

}
