package com.example.kalabalik

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Player::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val playerDao : PlayerDao

    //I denna klass har man ett object som är gemensamt för hela klassen
    //Alla i denna klass delar på detta object
    //Man får bara ett enda av companion object
    companion object {
        @Volatile // Detta är en kotlin annotation, betyder att Denna variabel cashas inte någon annan stans, den lagras direkt i minnet
        //Detta INSTANCE känns bara till inne i klassen och kan inte kommas åt utanför

        private var INSTANCE : AppDatabase? = null

        //Här bygger vi appdatabasen och får tillbaka den
        //Denna funktion kan kommas åt utifrån
        //Det ända som är public utåt är vår funktion
        fun getInstance(context: Context) :AppDatabase {
            //Syncornized inndebär att tråden inne i denna att detta är det enda som skall köras, nu har vi låst appen
            synchronized(this) {
                var instance = INSTANCE

                //Nu ska vi kolla om det redan finns en instans och då ska vi skapa den
                //Om det finns en instans så returnerar vi bara den som finns
                if (instance == null) {
                    //fallbackToDestructiveMigration() är till fall om vi ändrar hur tabellerna ser ut
                    //"När vi går upp en nivå i databasen så ändrar vi hur den ser ut"
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "players_database"
                    )
                        .fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}