package com.example.kalabalik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    lateinit var amountOfPlayers: EditText
    lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        amountOfPlayers = findViewById(R.id.amountOfPlayers)
        buttonNext = findViewById(R.id.buttonNext)

        buttonNext.setOnClickListener{
            buttonNextPage()
        }
    }
    fun buttonNextPage(){
        val amount = amountOfPlayers.text.toString().toInt()
        //När next button klickas kommer vi till nästa vy
        val intent = Intent(this, PlayerNames::class.java)
        intent.putExtra("amount", amount)
        startActivity(intent)
    }

   /* override fun onClick(v: View?) {
        when(amountOfPlayers?.id) {
            R.id.buttonNext -> {
                Log.d("!!!", "BUTTON KLICKED")
            }
            amountOfPlayers.apply {
                inputType = InputType.TYPE_CLASS_TEXT
                setText("")
            }*/
        //}
    //}




}