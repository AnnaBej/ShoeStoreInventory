package com.bejnarowicz.shoestoreinventory.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bejnarowicz.shoestoreinventory.database.ShoeDatabase
import com.bejnarowicz.shoestoreinventory.database.repository.ShoeRepository
import com.bejnarowicz.shoestoreinventory.database.model.Shoe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ShoeViewModel(application: Application) : AndroidViewModel(application) {

    val getAllShoes: LiveData<List<Shoe>>
    private val repository: ShoeRepository

    init {
        val shoeDao = ShoeDatabase.getDatabase(application).shoeDao()
        repository = ShoeRepository(shoeDao)
        getAllShoes = repository.getAllShoes
    }


    fun addShoe(shoe: Shoe) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addShoes(shoe)
        }
    }

    fun updateShoe(shoe: Shoe) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateShoe(shoe)
        }
    }

    fun deleteShoe(shoe: Shoe){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShoe(shoe)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }


}