package com.bejnarowicz.shoestoreinventory.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bejnarowicz.shoestoreinventory.database.model.Shoe

@Dao
interface ShoeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addShoes(shoe: Shoe)

    @Query("SELECT * FROM shoe_database ORDER BY id ASC")
    fun getAllShoes(): LiveData<List<Shoe>>

    @Update
    suspend fun updateShoe(shoe: Shoe)

    @Delete
    suspend fun deleteShoe(shoe: Shoe)

    @Query("DELETE FROM shoe_database")
    suspend fun deleteAll()



}