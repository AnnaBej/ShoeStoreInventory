package com.bejnarowicz.shoestoreinventory.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "shoe_database")
@Parcelize
data class Shoe(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var brand: String,
    var stock: String,
    var photo: String?, //image path
    var comment: String?

) : Parcelable