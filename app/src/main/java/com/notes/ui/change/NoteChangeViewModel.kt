package com.notes.ui.change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.data.NoteDbo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class NoteChangeViewModel @Inject constructor(private val noteDatabase: NoteDatabase) :
    ViewModel() {

    //    2021-11-30T17:29:22.908
    fun changeNote(id: Long, title: String, content: String, createdAt: LocalDateTime) {
        val localDateTime = LocalDateTime.now()
        viewModelScope.launch {
            val note = NoteDbo(
                id = id,
                title = title,
                content = content,
                createdAt = createdAt,
                modifiedAt = localDateTime
            )
            noteDatabase.noteDao().update(note)
        }
    }

    fun deleteEmptyNote(id: Long, title: String, content: String, createdAt: LocalDateTime) {
        val localDateTime = LocalDateTime.now()
        if (title.isEmpty() &&
            content.isEmpty()
        )
            viewModelScope.launch {
                val note = NoteDbo(
                    id = id,
                    title = title,
                    content = content,
                    createdAt = createdAt,
                    modifiedAt = localDateTime
                )
                noteDatabase.noteDao().delete(note)
            }
    }
}