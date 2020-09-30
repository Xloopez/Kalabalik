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
import androidx.core.view.isInvisible
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

    var consequenceOptionPoints = 0
    var counter = 0
    //var counterPlayer = 0
    //var amountOfRounds = GameSettings.amountOfRounds
    var currentRound = 1

    val listOfChoices = mutableListOf("Consequence", "Mission")
    //val name = GameSettings.listOfPlayers.get(counter)
    val name = GameSettings.listOfPlayers.get(counter).name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val name = GameSettings.listOfPlayers.get(counter).name
        displayPlayerName = findViewById(R.id.playerNameTurn)
        displayPlayerName.text = "$name:s tur!"

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

        //displayPlayerName.setText("$name: s tur!")
        //Setting the event listener

        rightButton.setOnClickListener {
            flipCard()
        }
    }
    fun scoreBoardActivity (){
        val intent = Intent(this, HighScoreActivity::class.java)
        startActivity(intent)
    }

    fun addRoundScoreFragment(){
            val roundScoreFragment = RoundFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.container, roundScoreFragment, "roundScoreFragment")
            transaction.commit()
            //RoundFragment().setContinueButton()
            //addRightButtonRoundFragment()
    }
    fun addRightButtonRoundFragment(){
        val RoundFragment = supportFragmentManager.findFragmentByTag("roundScoreFragment")
        val transaction = supportFragmentManager.beginTransaction()

        rightButton.setOnClickListener {
            transaction.remove(RoundFragment!!)
            transaction.commit()
        }
    }

    fun playerTurn() {
        val name = GameSettings.listOfPlayers.get(counter).name

        when (currentRound) {
            /*0 -> {
                increaseRounds()
                /*Log.d("!!!", "Round $currentRound")
                displayPlayerName.text = "$name:s tur!"*/

            }*/
            in 1 until GameSettings.amountOfRounds -> {
                Log.d("!!!", "Counter is: $counter")
               /* if (currentRound == GameSettings.amountOfRounds) {
                        Log.d("!!!", "LAST ROUND")
                        increaseCounterByOne()
                        displayPlayerName.text = "$name:s tur!"
                }*/
                displayPlayerName.text = "$name:s tur!"
                increaseCounterByOne()

                //displayPlayerName.text = "$name:s tur!"
                if (counter == GameSettings.playerCount) {
                    displayPlayerName.text = "$name:s tur!"

                    Log.d("!!!", "Round $currentRound")

                    for (i in 0 until GameSettings.listOfPlayers.size) {
                        Log.d("!!!", "${GameSettings.listOfPlayers[i].name}")
                        Log.d("!!!", "${GameSettings.listOfPlayers[i].points}")
                    }


                    restartcounter()
                    increaseRounds()
                } else if (currentRound == GameSettings.amountOfRounds) {
                    scoreBoardActivity()
                }
            }
        }
    }

    /*fun leftButtonFlipPard() {
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
    }*/
    fun flipCard() {
        when (isFront) {
            false -> {
                frontAnimation.setTarget(card_Front)
                backAnimation.setTarget(card_back)
                frontAnimation.start()
                backAnimation.start()
                isFront = false
                playerTurn()
                consequenceOrMission()
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

    fun increaseCounterByOne() {
        counter++
        //counterPlayer++
    }

    fun increaseRounds() {
        currentRound++
    }

    fun restartcounter() {
        counter = 0
        //counterPlayer= 0
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
                leftButton.setText("+$consequencePoints")

                leftButton.setOnClickListener {
                    leftButtonPoints((consequenceOptionPoints/2)-1)
                    flipCard()
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
        GameSettings.addPointsToPlayer(counter, points)
    }
    fun leftButtonPoints(points: Int){
        GameSettings.addPointsToPlayer(counter, points)
    }
    fun rightMissionButton(missionPoints: Int){
        leftButton.visibility = View.INVISIBLE
        rightButton.setText("+$missionPoints")
        GameSettings.addPointsToPlayer(counter, missionPoints)
    }

}