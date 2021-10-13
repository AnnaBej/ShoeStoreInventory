package com.bejnarowicz.shoestoreinventory.database.repository

import androidx.lifecycle.LiveData
import com.bejnarowicz.shoestoreinventory.database.ShoeDao
import com.bejnarowicz.shoestoreinventory.database.model.Shoe

class ShoeRepository(private val shoeDao: ShoeDao) {

    val getAllShoes: LiveData<List<Shoe>> = shoeDao.getAllShoes()

    suspend fun addShoes(shoe: Shoe) {
        shoeDao.addShoes(shoe)
    }

    suspend fun updateShoe(shoe: Shoe) {
        shoeDao.updateShoe(shoe)
    }

    suspend fun deleteShoe(shoe: Shoe) {
        shoeDao.deleteShoe(shoe)
    }

    suspend fun deleteAll() {
        shoeDao.deleteAll()
    }


}