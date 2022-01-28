package com.notes.data

import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    suspend fun getAll(): List<NoteDbo>

    @Insert
    suspend fun insertAll(vararg notes: NoteDbo)

    @Update
    suspend fun update(notes: NoteDbo)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById (id: Long)

}