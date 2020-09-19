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
import kotlinx.android.synthetic.main.activity_player.*
import kotlin.math.round

class GameActivity : AppCompatActivity() {
    //
    lateinit var frontAnimation: AnimatorSet
    lateinit var backAnimation: AnimatorSet
    var isFront = true

    //
    lateinit var displayPlayerName: TextView
    lateinit var btnNext: Button
    var counter = 0
    var amountOfRounds = GameSettings.amountOfRounds
    var currentRound = 1
    val  name = GameSettings.listOfPlayers.get(counter)//.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        btnNext = findViewById(R.id.btnNextPlayerTurn)
        displayPlayerName = findViewById(R.id.playerNameTurn)

        //Animator object
        //modified camera scale
        val scale: Float = applicationContext.resources.displayMetrics.density
        card_Front.cameraDistance = 8000 * scale
        card_back.cameraDistance = 8000 * scale

        //front animation
        frontAnimation = AnimatorInflater.loadAnimator(applicationContext,R.animator.front_animator) as AnimatorSet
        backAnimation = AnimatorInflater.loadAnimator(applicationContext,R.animator.back_animator) as AnimatorSet

        //Setting the event listener
        flip_btn.setOnClickListener{
            flipCard()
        }

        btnNext.setOnClickListener{
            playerTurn()
        }
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
    fun flipCard(){
        if(isFront) {
            frontAnimation.setTarget(card_Front)
            backAnimation.setTarget(card_back)
            frontAnimation.start()
            backAnimation.start()
            isFront = false
        } else {
            frontAnimation.setTarget(card_back)
            backAnimation.setTarget(card_Front)
            backAnimation.start()
            frontAnimation.start()
            isFront = true
        }
    }

    fun increaseCounterByOne(){
        counter++
    }
    fun increaseRounds(){
        currentRound++
    }
    fun restartcounter(){
        counter = 0
    }
    fun rightClick(points: Int){

    }
}
