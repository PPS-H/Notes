package com.example.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Notes::class],
    version = 1
)
abstract class NotesDataBase: RoomDatabase() {
    abstract fun getDao():NotedDAO

    companion object {
        //To make the instance immediately available for all the coroutines...make it volatile
        @Volatile
        private var INSTANCE:NotesDataBase?=null
        private var LOCK=Any()
        //id the INSTANCE is not null then it will return INSTANCE but if null then synchronized block will be executed
        operator fun invoke(context: Context)= INSTANCE?: synchronized(LOCK){
            INSTANCE?: buildDataBase(context).also {
                INSTANCE=it
            }
        }
        fun buildDataBase(context: Context)=Room.databaseBuilder(
            context.applicationContext,
            NotesDataBase::class.java,
            "MydataBase"
        ).build()
    }
}