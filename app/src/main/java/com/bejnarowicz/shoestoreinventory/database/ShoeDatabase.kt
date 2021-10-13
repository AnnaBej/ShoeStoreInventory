package com.bejnarowicz.shoestoreinventory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bejnarowicz.shoestoreinventory.database.model.Shoe

@Database(entities = [Shoe::class], version = 4, exportSchema = false)
abstract class ShoeDatabase : RoomDatabase() {

    abstract fun shoeDao() : ShoeDao

    companion object {

        @Volatile
        private var INSTANCE: ShoeDatabase? = null

        fun getDatabase(context: Context): ShoeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoeDatabase::class.java,
                    "shoe_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}