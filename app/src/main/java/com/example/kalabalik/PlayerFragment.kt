package com.example.kalabalik

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_player.*


class PlayerFragment : Fragment() {

    lateinit var playerName: EditText
    lateinit var addPlayerNameBtn: Button

    var counter = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_player, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerName = view.findViewById(R.id.personName1)
        addPlayerNameBtn = view.findViewById(R.id.buttonNextName)

        //onClick(view)
        addPlayerNameBtn.setOnClickListener{
            Log.d("!!!", "Pressed")
            Log.d("!!!", "Counter: $counter")
            playersNames()
        }
    }

    /*fun onClick(v: View){
        when (v?.id) {
            R.id.buttonNextName -> {playersNames()}
        }
    }*/

    fun playersNames(){
        val  name = playerName.text.toString()
        Log.d("!!!", "Name: $name")

        when (counter){
            in 1 until GameSettings.playerCount -> {
                GameSettings.addPlayerToList(name)
                //addPlayer(name)
                increaseCounterByOne()
                playerName.text.clear()
                playerName.setHint("Spelare $counter")
                //Log.d("!!!", "$name")
                when (counter){
                    GameSettings.playerCount -> buttonNextName.setText(R.string.button_start_game)  //buttonNextName.setText("Starta spelet")
                }
            }
            GameSettings.playerCount -> {
                GameSettings.addPlayerToList(name)
                //addPlayer(name)
                increaseCounterByOne()
                //val intent = Intent(this, GameActivity::class.java)
                //startActivity(intent)
            }
        }
    }

    fun increaseCounterByOne(){
        counter++
    }



}