package com.example.kalabalik

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_high_score.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class HighScoreActivity : AppCompatActivity(), CoroutineScope {

    lateinit var replayBtn: Button

    lateinit var recyclerView: RecyclerView

    lateinit var job: Job
    //private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var db : AppDatabase //Klassen som vi skapade

    //var playerList: PlayerList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        job = Job()

        //applicationcontext = Hela appen, inte bara den här klassen (this)
        //fallbackToDestructiveMigration() = förstör den gamla datan om vi skapar en ny data -
        //Om man ändrar strukturen på hur datan ska lagras så raderas den gamla
        db = AppDatabase.getInstance(this)

        //Letar efter vår recyclerView
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        //Skapar vår adapter
        val adapter = HighScoreViewAdapter(this, GameSettings.listOfPlayers)

        //Kopplar recycklen med adaptern
        recyclerView.adapter = adapter

        replayBtn = findViewById(R.id.playAgainButton)

        replayBtn.setOnClickListener {
            playAgain(this)
        }

        for (player in GameSettings.listOfPlayers)
            savePlayer(player)
    }

    override fun onResume() {
        super.onResume()

        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun playAgain(context: Context) {
        startActivity(Intent(this, MainActivity::class.java))
        this.finishActivity(0)
    }

    fun savePlayer(player: Player) {
        //Den här raden kmr ta tid och blocka och istället vill vi starta en coorutin som låtr detta göras parallelt
        //Man kan starta en coorutin genom att göra en launch eller async
        //Launch skapar man bara en corutin som gör vad den vill och man bryr sig inte om att få tillbaka någonting
        //När vi läser så vill vi ha en async istället
        //För att skapa en coorutin så behöver vi en coorutin scoope
        //Global scope som är gemensamt för hela appen

        //Inom parentesen säger vi att vi inte vill göra detta på ghuvudtråden
        //Dispatcher main är huvudtråden - det vi vill undvika
        //VI vill att det inte ska köras på huvudtråden utan i dispatchers.IO

        GlobalScope.launch (Dispatchers.IO) {
            db.playerDao.insert(player)
        }
    }
}