package com.notes.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.data.NoteDbo
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class NoteCreateViewModel @Inject constructor
    (private val noteDatabase: NoteDatabase) : ViewModel() {

    fun createNote(title: String, content: String) {
        val localDateTime = LocalDateTime.now()
        viewModelScope.launch {
            val note = NoteDbo(
                title = title,
                content = content,
                createdAt = localDateTime,
                modifiedAt = localDateTime
            )
            noteDatabase.noteDao().insertAll(note)
        }
    }
}