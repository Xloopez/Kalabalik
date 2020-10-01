package com.example.kalabalik

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    //
    lateinit var frontAnimation: AnimatorSet
    lateinit var backAnimation: AnimatorSet
    var isFront = false

    //
    lateinit var displayPlayerName: TextView
    lateinit var backCardText: TextView
    lateinit var frontCardText: TextView

    lateinit var arrayConsequence: Array<String>
    lateinit var arrayConsequencePoints: IntArray
    lateinit var arrayMission: Array<String>
    lateinit var arrayMissionPoints: IntArray

    lateinit var rightButton: Button
    lateinit var leftButton: Button
    lateinit var playerName: String

    var consequenceOptionPoints = 0
    var turn = 0
    var currentRound = 1

    val listOfChoices = mutableListOf("Consequence", "Mission")
    val finalRoundReached = GameSettings.amountOfRounds.plus(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Animator object
        //modified camera scale
        val scale: Float = applicationContext.resources.displayMetrics.density
        card_Front.cameraDistance = 8000 * scale
        card_back.cameraDistance = 8000 * scale

        //front animation
        frontAnimation = AnimatorInflater.loadAnimator(
            applicationContext,
            R.animator.front_animator
        ) as AnimatorSet
        backAnimation = AnimatorInflater.loadAnimator(
            applicationContext,
            R.animator.back_animator
        ) as AnimatorSet

        backCardText = findViewById(R.id.card_back)
        frontCardText = findViewById(R.id.card_Front)

        arrayConsequence = resources.getStringArray(R.array.Consequence)
        arrayConsequencePoints = resources.getIntArray(R.array.ConsequencePoints)

        arrayMission = resources.getStringArray(R.array.Mission)
        arrayMissionPoints = resources.getIntArray(R.array.MissionPoints)

        rightButton = findViewById(R.id.right_btn)
        leftButton = findViewById(R.id.left_btn)

        displayPlayerName = findViewById(R.id.playerNameTurn)

        displayPlayerName.text = "${GameSettings.listOfPlayers[0].name}:s tur!"

        rightButton.setOnClickListener {
            flipCard()
            playerTurn()
        }
    }
    fun scoreBoardActivity (){
        val intent = Intent(this, HighScoreActivity::class.java)
        startActivity(intent)
    }

    fun playerTurn() {

        val roundInterval = (1..GameSettings.amountOfRounds)
        //val finalRoundReached = GameSettings.amountOfRounds.plus(1)

        when (currentRound) {
            0 -> {
                increaseRounds()
                Log.d("!!!", "Round $currentRound")
            }
            in roundInterval -> {
                turn++
                if (turn == GameSettings.listOfPlayers.count().plus(1)) {
                    increaseRounds()
                    restartcounter()

                    playerName = GameSettings.listOfPlayers[turn-1].name
                    displayPlayerName.text = "${playerName}:s tur!"

                    consequenceOrMission()

                    Log.d("!!!", "Turn non-existing: increasingRounds() & restartcounter(): ${GameSettings.listOfPlayers.count().plus(1)}")
                } else {
                    playerName = GameSettings.listOfPlayers[turn-1].name
                    displayPlayerName.text = "${playerName}:s tur!"

                    consequenceOrMission()
                }

                Log.d("!!!", "Player: ${playerName}! Round: $currentRound Turn: $turn")

                if (currentRound == finalRoundReached){
                    Log.d("!!!", "Next Activity")
                    scoreBoardActivity()
                }
            }
        }
    }

    fun flipCard() {
        when (isFront) {
            false -> {
                frontAnimation.setTarget(card_Front)
                backAnimation.setTarget(card_back)
                frontAnimation.start()
                backAnimation.start()
                isFront = false
                //playerTurn()
                //consequenceOrMission()
            }
            true -> {
                frontAnimation.setTarget(card_back)
                backAnimation.setTarget(card_Front)
                backAnimation.start()
                frontAnimation.start()
                isFront = false
            }
        }
    }
    fun increaseRounds() {
        currentRound++
    }

    fun restartcounter() {
        turn = 1
    }

    fun consequenceOrMission() {
        //val randomIndex = mutableListOf<String>(arrayConsequence, arrayMission).random()

        val randomC = listOfChoices.random()
        //Log.d("!!!", "randomC: $randomC")

        when (randomC) {
            "Consequence" -> {
                val randomConsequenceIndex = (0 until arrayConsequence.count()).random()
                //Log.d("!!!", "ranCIdex: $randomConsequenceIndex")

                val consequenceStr = arrayConsequence[randomConsequenceIndex]
                val consequencePoints = arrayConsequencePoints[randomConsequenceIndex]
                val consequenceOption = consequenceChoice(randomConsequenceIndex)
                //Log.d("!!!", "$consequenceStr")
                //Log.d("!!!", "$consequencePoints")

                //Front card text
                frontCardText.text = "Konsekvens"
                //Back card text
                backCardText.setText("$consequenceStr \n+$consequencePoints poäng" +
                        "\n \nEller\n \n $consequenceOption \n+$consequenceOptionPoints poäng")

                rightButtonPoints(consequencePoints)

                leftButton.visibility = View.VISIBLE
                leftButton.setText("+${consequenceOptionPoints}")

                leftButton.setOnClickListener {
                    leftButtonPoints((consequenceOptionPoints/2)-1)
                    flipCard()
                    playerTurn()
                }
            }
            "Mission" -> {
                val randomMissionIndex = (0 until arrayMission.count()).random()
                //Log.d("!!!", "ranCIdex: $randomMissionIndex")

                val missionStr = arrayMission[randomMissionIndex]
                val missionPoints = arrayMissionPoints[randomMissionIndex]
                //Log.d("!!!", "$missionStr")
                //Log.d("!!!", "$missionPoints")

                frontCardText.text = "Uppdrag"
                backCardText.setText("$missionStr \n+$missionPoints poäng")
                rightMissionButton(missionPoints)
            }
        }
    }

    fun consequenceChoice(index: Int): String {

        var consequenceCoiceStr = ""

        if (index.plus(1) % 2 != 0) {
            consequenceCoiceStr = arrayConsequence[index.plus(1)]
            consequenceOptionPoints = arrayConsequencePoints[index.plus(1)]
            return consequenceCoiceStr
        } else if (index.plus(1) % 2 == 0){
            consequenceCoiceStr = arrayConsequence[index - 1]
            consequenceOptionPoints = arrayConsequencePoints[index - 1]
        }

        return consequenceCoiceStr
    }

    fun rightButtonPoints(points: Int){
        leftButton.visibility = View.VISIBLE
        rightButton.setText("+$points")
        GameSettings.addPointsToPlayer(turn-1, points)
    }
    fun leftButtonPoints(points: Int){
        GameSettings.addPointsToPlayer(turn-1, points)
    }
    fun rightMissionButton(missionPoints: Int){
        leftButton.visibility = View.INVISIBLE
        rightButton.setText("+$missionPoints")
        GameSettings.addPointsToPlayer(turn-1, missionPoints)
    }

}