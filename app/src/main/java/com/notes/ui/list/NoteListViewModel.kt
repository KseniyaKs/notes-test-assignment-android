package com.notes.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import kotlinx.coroutines.launch
import java.io.Serializable
import java.time.LocalDateTime
import javax.inject.Inject

class NoteListViewModel @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModel() {

    private val _notes = MutableLiveData<List<NoteListItem>?>()
    val notes: LiveData<List<NoteListItem>?> = _notes

    private val _navigateToNoteCreation = MutableLiveData<Unit?>()
    val navigateToNoteCreation: LiveData<Unit?> = _navigateToNoteCreation

    private val _navigateToNoteChange = MutableLiveData<NoteListItem>()
    val navigateToNoteChange: LiveData<NoteListItem> = _navigateToNoteChange

    init {
        viewModelScope.launch{
            getAllItemList()
        }
    }

    fun onCreateNoteClick() {
        _navigateToNoteCreation.postValue(Unit)
    }

    fun onChangeNoteClick(noteListItem: NoteListItem){
        _navigateToNoteChange.postValue(noteListItem)
    }

    fun deleteNote(noteListItem: NoteListItem){
        viewModelScope.launch {
            noteDatabase.noteDao().deleteById(noteListItem.id)
            getAllItemList()
        }
    }

    suspend fun getAllItemList(){
        _notes.postValue(
            noteDatabase.noteDao().getAll()
                .sortedBy { it.modifiedAt }
                .reversed()
                .map {
                NoteListItem(
                    id = it.id,
                    title = it.title,
                    content = it.content,
                    createdAt = it.createdAt
                )
            }
        )
    }
}

data class NoteListItem (
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime
) : Serializable