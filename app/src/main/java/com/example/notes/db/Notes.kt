package com.example.notes.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Notes(
    var note: String
):Parcelable{
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
