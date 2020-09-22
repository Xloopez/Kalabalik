package com.example.kalabalik

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    //
    lateinit var frontAnimation: AnimatorSet
    lateinit var backAnimation: AnimatorSet
    var isFront = true

    //
    lateinit var displayPlayerName: TextView
    lateinit var backCardText: TextView
    lateinit var frontCardText: TextView

    lateinit var arrayConsequence: Array<String>
    lateinit var arrayConsequencePoints: IntArray
    lateinit var arrayMission: Array<String>
    lateinit var arrayMissionPoints: IntArray
    //lateinit var consequenceOptionPoints: Int
    //lateinit var randomConsequenceIndex: Int

    //val randomConsequenceIndex = (0 until arrayConsequence.count()).random()
    val listOfChoices = mutableListOf("Consequence", "Mission")

    var consequenceOptionPoints = 0

    var counter = 0
    var amountOfRounds = GameSettings.amountOfRounds
    var currentRound = 1
    val name = GameSettings.listOfPlayers.get(counter)//.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        displayPlayerName = findViewById(R.id.playerNameTurn)

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

        //randomConsequenceIndex = (0 until arrayConsequence.count()).random()


        //displayPlayerName.setText("$name: s tur!")
        //Setting the event listener
        flip_btn.setOnClickListener {
            flipCard()
        }

        /*btnNext.setOnClickListener{
            playerTurn()
        }*/
    }

    fun playerTurn() {
        val name = GameSettings.listOfPlayers.get(counter)//.text.toString()
        when (currentRound) {
            1 -> {
                Log.d("!!!", "Round $currentRound")
                increaseRounds()
            }
            in 2 until amountOfRounds -> {
                //Log.d("!!!", "Round $currentRound")
                increaseCounterByOne()
                displayPlayerName.setText("$name:s tur!")

                //increaseCounterByOne()
                if (counter == GameSettings.playerCount) {
                    Log.d("!!!", "Round $currentRound")
                    increaseRounds()
                    restartcounter()
                }

                when (currentRound) {
                    amountOfRounds -> {
                        Log.d("!!!", "Last Round!")
                        displayPlayerName.setText("$name:s tur!")
                        increaseCounterByOne()
                    }
                }
            }
        }
    }

    fun flipCard() {
        if (isFront) {
            playerTurn()
            frontAnimation.setTarget(card_Front)
            backAnimation.setTarget(card_back)
            frontAnimation.start()
            backAnimation.start()
            isFront = false
            consequenceOrMission()
        } else {
            frontAnimation.setTarget(card_back)
            backAnimation.setTarget(card_Front)
            backAnimation.start()
            frontAnimation.start()
            isFront = true
        }
    }

    fun increaseCounterByOne() {
        counter++
    }

    fun increaseRounds() {
        currentRound++
    }

    fun restartcounter() {
        counter = 0
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
                        "\n \nEller\n \n $consequenceOption \n+$consequenceOptionPoints poäng" )
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
            }
        }
    }

    fun consequenceChoice(index: Int): String {

        var consequenceCoiceStr = ""

        if (index.plus(1) % 2 != 0) {
            consequenceCoiceStr = arrayConsequence[index.plus(1)]
            consequenceOptionPoints = arrayConsequencePoints[index.plus(1)]
            return consequenceCoiceStr
        } else {
            consequenceCoiceStr = arrayConsequence[index - 1]
            consequenceOptionPoints = arrayConsequencePoints[index - 1]
        }

        return consequenceCoiceStr
    }
}


        //

        /*when(randomIndex) {
            arrayConsequence -> {
                frontCardText.setText("Consequence")
                backCardText.setText(randomIndex + "\n \nEller\n \n" + arrayConsequence)
            }
            arrayMission -> {
                frontCardText.setText("Mission")
                backCardText.setText(randomIndex)
            }
        }*/

