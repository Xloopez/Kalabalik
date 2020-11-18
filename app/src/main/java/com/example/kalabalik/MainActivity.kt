package com.example.kalabalik

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    lateinit var playerAmount: EditText
    lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        highScoreListFragment()

        playerAmount = findViewById(R.id.amountOfPlayers)
        buttonNext = findViewById(R.id.buttonNext)

        buttonNext.setOnClickListener{
            buttonNextPage()
            buttonNext.visibility = View.INVISIBLE
            removeHighScoreListFragment()
        }
    }

    fun buttonNextPage(){
        GameSettings.playerCount = playerAmount.text.toString().toInt()

        val playerFragment = PlayerFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(R.id.playerFragmentContainer, playerFragment, "playerFragment")
        transaction.commit()

    }

    fun highScoreListFragment () {
        val highScoreListFragment = HighScoreListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.highScoreListFragment, highScoreListFragment, "highScoreListFragment")
        transaction.commit()
    }

    fun removeHighScoreListFragment() {
        val highScoreListFragment = supportFragmentManager.findFragmentByTag("highScoreListFragment")

        if (highScoreListFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(highScoreListFragment)
            transaction.commit()
        } else {
            Toast.makeText(this, "High score fragment not found", Toast.LENGTH_SHORT).show()
        }
    }
}