package com.example.kalabalik

import android.content.ClipData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {

    @Insert
    fun insert (item: Item)

    @Delete
    fun delete (item: Item)

    @Query("SELECT * FROM item WHERE score")
    fun loadByScore() : List<Item>

    @Query ("SELECT * FROM item") //jag vill ha ut allting från databasen som har en item
    fun getAll () : List<Item>


}