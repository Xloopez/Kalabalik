package com.example.kalabalik

import androidx.room.*

@Dao
interface PlayerDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(player: Player)

    @Delete
    fun delete(player: Player)

    @Query("SELECT * FROM player ORDER BY points DESC") //LIMIT 10
    fun getAll() : List<Player>
}