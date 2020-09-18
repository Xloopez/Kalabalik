package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.myapplication.databinding.ActivityGamingBinding

class GamingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvCard: AppCompatTextView

    private lateinit var rgPlayers: RadioGroup

    private lateinit var btnSuccess: AppCompatButton
    private lateinit var btnFail: AppCompatButton

    private lateinit var binding: ActivityGamingBinding

    private val arrayOfEnRandoms = EnRandom.values()
    private val listOfConsequences by lazy { resources.getStringArray(R.array.Consequences) }
    private val listOfMissions by lazy { resources.getStringArray(R.array.Mission) }
    private val listOfConsequencesPoints by lazy { resources.getIntArray(R.array.ConsequencesPoints) }
    private val listOfMissionsPoints by lazy { resources.getIntArray(R.array.MissionPoints) }

    private var pCount = GameSettings.playerCount
    private var totalRounds = GameSettings.amountOfRounds
    private var currRound = 1
    private var currTurn = 0
    private var pointsToAdd: Double = 0.0

    private lateinit var currPlayer: Player

    private var newTitle: String = ""

    private enum class EnOperation { SUCCESS, FAIL; }
    private enum class EnRandom { CONSEQUENCES, MISSION; }

    //TODO Button Cancel before done? Show final score?
    //TODO Override backbutton?

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityGamingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Game started"
        applyViewBinding()
        btnSuccess.setOnClickListener(this)
        btnFail.setOnClickListener(this)

        addPlayersToRadioGroup()
        nextPlayerTurn()
        titleViewUpdateRounds()
    }

    private fun applyViewBinding(){
        binding.apply {

            /* TEXTVIEWS */
            tvTitle = textViewTitle
            tvCard = textViewGamingInfo

            /* RADIOGROUPS */
            rgPlayers = radioGroupPlayers

            /* BUTTONS */
            btnSuccess = buttonSuccess
            btnFail = buttonFail

        }
    }

    private fun isFinalRound(): Boolean = (currRound == totalRounds) && (currPlayer.playerNum == 1)

    private fun displayFinalRoundStarted(){
        Log.d("!", "Final Round")
    }

    private fun addPlayersToRadioGroup() {

        GameSettings.listOfPlayers.forEach { p ->

            val themeWrapper = ContextThemeWrapper(this, R.style.RadioButtonStyle)
            val rb = AppCompatRadioButton(themeWrapper)

            rb.apply {
                text = p.name
                isClickable = false
                tag = p.playerNum
                buttonDrawable = StateListDrawable()
                setOnCheckedChangeListener { compoundButton, b ->
                    when(b){
                        true -> {
                            compoundButton.apply {
                                setBackgroundColor(Color.DKGRAY)
                                textSize = 16f
                                setTextColor(Color.WHITE)
                            }
                        }
                        false -> {
                            compoundButton.apply {
                                setBackgroundColor(Color.TRANSPARENT)
                                textSize = 12f
                                setTextColor(Color.BLACK)
                            }
                        }
                    }
                }
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
        tvCard.text = newCard /* Update card-info */
        currPlayer = GameSettings.listOfPlayers[currTurn - 1] /* Return(Current Turn - 1) because of ListIndex starts from 0 = 1, 1 = 2 etc.  */
        activatePlayerRadioBtn(playerNum = currPlayer.playerNum)  /* Activate tagged RadioButton by TAG from(currPlayer) */
    }

    private fun nextRound(){
        currTurn = 0 /* Reset to 0 when new round, btnClick adds 1 directly = Turn 1 on round start */
        currRound++
    }

    private fun titleViewUpdateRounds(){
        newTitle = getString(R.string.now_playing_round, currRound, totalRounds)
        tvTitle.text = newTitle
    }

    private fun randomizeCard() = when (arrayOfEnRandoms.random()) {
        EnRandom.CONSEQUENCES -> {

            val r = (0 until listOfConsequences.count()).random()
            pointsToAdd = listOfConsequencesPoints[r].toDouble()
            //Log.d("!", "$r Con: $pointsToAdd")
            listOfConsequences[r]

        }
        EnRandom.MISSION -> {

            val r = (0 until listOfMissions.count()).random()
            pointsToAdd = listOfMissionsPoints[r].toDouble()
            //Log.d("!", "$r Miss: $pointsToAdd")
            listOfMissions[r]

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_Success -> { doNext(EnOperation.SUCCESS) }
            R.id.button_Fail -> { doNext(EnOperation.FAIL) }
        }
    }

    private fun doNext(operation: EnOperation) {

        currPlayer.listAddRoundAndPoints(
             when (operation) {
                EnOperation.SUCCESS -> Pair(currRound, pointsToAdd)
                EnOperation.FAIL -> Pair(currRound, -1.0)
            }
        )

        when (currTurn == pCount) {
            true -> {
                nextRound()
                titleViewUpdateRounds()
            }
        }

        when (currRound) {
            in 1..totalRounds -> {

                nextPlayerTurn()
                when(isFinalRound()) { true -> displayFinalRoundStarted() }
                Animationz.slideOutRightSlideInLeft(tvCard)
                Log.d("!", "Round $currRound - Turn $currTurn - Player ${currPlayer.playerNum} ")

            }
            totalRounds.plus(1) -> {
                //TODO start next activity
                Log.d("!", "GAME ENDED")
//                for(p in GameSettings.listOfPlayers){
//                    Log.d("!", "${p.name} ${p.listOfRoundAndPoints}")
//                }
                btnSuccess.visibility = View.GONE
                btnFail.visibility = View.GONE
            }
        }
    }

}
