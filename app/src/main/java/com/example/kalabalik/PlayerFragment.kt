package com.example.kalabalik

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentActivity


class PlayerFragment : Fragment() {

    lateinit var playerName: EditText
    lateinit var addPlayerNameBtn: Button
    lateinit var rootview: View

    var counter = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_player, container, false)
        rootview = view
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerName = view.findViewById(R.id.personName1)
        addPlayerNameBtn = view.findViewById(R.id.buttonNextName)

        //onClick(view)
        addPlayerNameBtn.setOnClickListener{
            //Log.d("!!!", "Pressed")
            //Log.d("!!!", "Counter: $counter")
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
        //Log.d("!!!", "Name: $name")

        when (counter){
            in 1 until GameSettings.playerCount -> {
                GameSettings.addPlayerToList(name)
                //addPlayer(name)
                increaseCounterByOne()
                playerName.text.clear()
                playerName.setHint("Spelare $counter")
                //Log.d("!!!", "$name")
                when (counter){
                    GameSettings.playerCount -> {
                        addPlayerNameBtn.setText(R.string.button_start_game)
                    }  //buttonNextName.setText("Starta spelet")
                }
            }
            GameSettings.playerCount -> {
                GameSettings.addPlayerToList(name)

                increaseCounterByOne()

                startGameFragment()
                hidekeyBoardPlease(requireActivity(), playerName)

            }
        }
    }

    fun hidekeyBoardPlease(view1: FragmentActivity, view: View){
        (view1.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun increaseCounterByOne(){
        counter++
    }

    fun startGameFragment(){
        val gameFragment = GameFragment()
        val transaction = activity?.supportFragmentManager!!.beginTransaction()

        transaction.replace(R.id.playerFragmentContainer, gameFragment, "gameFragment")
        transaction.commit()
    }
    fun endPlayerFragment(){
        val playerFragment = PlayerFragment()
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.remove(playerFragment)
        transaction.commit()
    }



}