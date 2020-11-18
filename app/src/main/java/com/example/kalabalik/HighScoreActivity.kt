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

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private lateinit var db : AppDatabase //Klassen som vi skapade


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        job = Job()

        //applicationcontext = Hela appen, inte bara den här klassen (this)
        //fallbackToDestructiveMigration() = förstör den gamla datan om vi skapar en ny data -
        //Om man ändrar strukturen på hur datan ska lagras så raderas den gamla
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "high-score-list")
            .fallbackToDestructiveMigration().build()

        //Letar efter vår recyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        //Skapar vår adapter
        val adapter = HighScoreViewAdapter(this, GameSettings.listOfPlayers)

        //Kopplar recycklen med adaptern
        recyclerView.adapter = adapter

        replayBtn = findViewById(R.id.playAgainButton)

        replayBtn.setOnClickListener {
            playAgain(this)
        }



        for (i in 0..GameSettings.listOfPlayers.size) {
            val item = Item(0, GameSettings.listOfPlayers[i].name, GameSettings.listOfPlayers[i].points)
            saveItem(item)
        }

        val listOfScoreFromDb = loadAll()

        GlobalScope.launch(Dispatchers.Main) {
            //Vi tar listan och väntar på den
            val itemsList = listOfScoreFromDb.await()

            GameSettings.listOfPlayersHighscore = itemsList

            for (item in itemsList) {
                Log.d("!!!", "item: $item")
            }

            //GameSettings.listOfPlayersHighscore = listOfScoreFromDb

        }
    }

    override fun onResume() {
        super.onResume()

        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun playAgain(context: Context) {
        startActivity(Intent(this, MainActivity::class.java))
        this.finishActivity(0)
    }

    fun saveItem(item: Item) {
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
            db.itemDao().insert(item)
        }
    }

    fun loadAll() : Deferred<List<Item>> =
        //Nu vill jag ha tillbaka något och jag skriver async
        //Kmr göras paralellt med det andra
        GlobalScope.async (Dispatchers.IO){
            db.itemDao().getAll()

    }



}