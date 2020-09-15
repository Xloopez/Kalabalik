package com.example.kalabalik

import android.location.GnssMeasurement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {
    lateinit var playerAmount: EditText
    lateinit var playerName: EditText
    lateinit var buttonStartG: Button
    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerAmount = findViewById(R.id.personName1)
        buttonStartG = findViewById(R.id.buttonStartGame)
        buttonStartG.setOnClickListener{
        }

    }



    override fun onClick(v: View?) {

        when(v?.id) {
            R.id.buttonNext -> {

                when (counter) {
                    0 -> {
                        GameSettings.playerCount = Integer.parseInt(playerAmount.text.toString())
                        increaseCounterByOne()
                    }
                    in 1 until GameSettings.playerCount -> {
                        var playerNames = findViewById<>(R.)

                    }



                }
            }
        }
    }
    fun increaseCounterByOne(){
        counter++
    }
}










/*val amountOfPlayers = intent.getIntExtra("amount", 0)

when (amountOfPlayers) {
    1 -> {
        personName1.visibility = View.VISIBLE
        personName2.visibility = View.INVISIBLE
        personName3.visibility = View.INVISIBLE
        personName4.visibility = View.INVISIBLE
        personName5.visibility = View.INVISIBLE
    }
    2 -> {
        personName1.visibility = View.VISIBLE
        personName2.visibility = View.VISIBLE
        personName3.visibility = View.INVISIBLE
        personName4.visibility = View.INVISIBLE
        personName5.visibility = View.INVISIBLE
    }
    3 -> {
        personName1.visibility = View.VISIBLE
        personName2.visibility = View.VISIBLE
        personName3.visibility = View.VISIBLE
        personName4.visibility = View.INVISIBLE
        personName5.visibility = View.INVISIBLE
    }
    4 -> {
        personName1.visibility = View.VISIBLE
        personName2.visibility = View.VISIBLE
        personName3.visibility = View.VISIBLE
        personName4.visibility = View.VISIBLE
        personName5.visibility = View.INVISIBLE
    }
    5 -> {
        personName1.visibility = View.VISIBLE
        personName2.visibility = View.VISIBLE
        personName3.visibility = View.VISIBLE
        personName4.visibility = View.VISIBLE
        personName5.visibility = View.VISIBLE
    }

}

}

fun addNames() {
    val name1 = findViewById<EditText>(R.id.personName1)
    val name2 = findViewById<EditText>(R.id.personName2)
    val name3 = findViewById<EditText>(R.id.personName3)
    val name4 = findViewById<EditText>(R.id.personName4)
    val name5 = findViewById<EditText>(R.id.personName5)

    nameList.add(name1)
    nameList.add(name2)
    nameList.add(name3)
    nameList.add(name4)
    nameList.add(name5)
}*/