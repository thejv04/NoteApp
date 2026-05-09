package com.thejv04.noteapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.thejv04.noteapp.model.Note
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing note data in the UI layer.
 * Extends [AndroidViewModel] to have access to the application context
 * needed for database initialization.
 *
 * Exposes a [StateFlow] of notes that the UI observes for changes,
 * and provides methods to insert, update and delete notes.
 *
 * @param application The application instance used to initialize the database.
 */

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    /**
     * A [StateFlow] that emits the current list of all notes.
     * Starts collecting when there is at least one subscriber
     * and stops 5 seconds after the last subscriber disappears.
     */

    val allNotes: StateFlow<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    /**
     * Inserts a new note into the database.
     * Runs in the [viewModelScope] coroutine scope.
     *
     * @param note The note to insert.
     */

    fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    /**
     * Updates an existing note in the database.
     * Runs in the [viewModelScope] coroutine scope.
     *
     * @param note The note with updated values.
     */

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }

    /**
     * Deletes a note from the database.
     * Runs in the [viewModelScope] coroutine scope.
     *
     * @param note The note to delete.
     */

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }
}