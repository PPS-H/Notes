package com.example.notes.db


import androidx.room.*

@Dao
interface NotedDAO {
    @Insert
    suspend fun addNotes(note:Notes)

    @Query("SELECT * FROM notes")
    suspend fun getAllNotes():List<Notes>

    @Update
    suspend fun updateNotes(note: Notes)

    @Delete
    suspend fun delete(note: Notes)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}