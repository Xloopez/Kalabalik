package com.example.kalabalik

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_high_score.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope{

    lateinit var playerAmount: EditText
    lateinit var buttonNext: Button

    private lateinit var db: AppDatabase
    private lateinit var job: Job

    var highScoreListFragment: HighScoreListFragment? = null


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job = Job()
        db = AppDatabase.getInstance(this)

        /*addNewPlayer(Player(0, "nicole", 10))
        addNewPlayer(Player(0, "Tina", 4))
        addNewPlayer(Player(0, "Ivan", 8))
        addNewPlayer(Player(0, "Jakline", 14))
        addNewPlayer(Player(0, "Daniella", 2))
        addNewPlayer(Player(0, "Jessica", 9))*/

        loadAllPlayers()

        highScoreListFragment()

        playerAmount = findViewById(R.id.amountOfPlayers)
        buttonNext = findViewById(R.id.buttonNext)

        amountOfPlayers.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> removeHighScoreListFragment()
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        buttonNext.setOnClickListener{
            buttonNextPage()
            buttonNext.visibility = View.INVISIBLE
            //removeHighScoreListFragment()
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
        highScoreListFragment = HighScoreListFragment()
        val transaction = supportFragmentManager.beginTransaction()

        //Null check
        transaction.add(R.id.highScoreListFragment, highScoreListFragment!!, "highScoreListFragment")
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

    //Diferred = avstannad/ Uppskuten/ Avvaktande
    fun loadAllPlayers() {
        //Denna funktion läser från vår databas
        //Global scope = gemensamt för hela appen
        val player = async (Dispatchers.IO){
            db.playerDao.getAll()
        }
        Log.d("!!!", "$player")

        //Vi väntar på att hela listan lästs in och sen så tar vi listan och skickar med det vi har läst från vår databas
        launch{
            val list = player.await().toMutableList()
            GameSettings.listOfPlayersHighscore = list

            //Log.d("!!!", "High score list fragment $highScoreListFragment")
            //Log.d("!!!", "High score list recycler view ${highScoreListFragment?.highScoreRecyclerView}")
            //Log.d("!!!", "High score list adapter ${highScoreListFragment?.highScoreRecyclerView?.adapter}")

            highScoreListFragment?.recyclerView()
            GameSettings.highestScoreSorted(GameSettings.listOfPlayersHighscore)

            for (player in GameSettings.listOfPlayersHighscore) {
                Log.d("!!!", "PLAYERS: ${player}")
            }
        }

    }

    fun addNewPlayer(player: Player) {
        GameSettings.listOfPlayersHighscore.add(player)

        //Detta skall göras asyncront - vi ska starta en coroutine
        launch(Dispatchers.IO) {
            db.playerDao.insert(player)
        }
    }

}