package com.thejv04.noteapp.data

import com.thejv04.noteapp.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that acts as a single source of truth for note data.
 * Abstracts the data layer from the rest of the application,
 * providing a clean API for data access to the ViewModel.
 *
 * @param noteDao The DAO used to perform database operations.
 */

class NoteRepository(private val noteDao: NoteDao) {

    /**
     * A [Flow] that emits the list of all notes ordered by timestamp descending.
     * Automatically updates when the database changes.
     */

    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    /**
     * Inserts a new note into the database.
     *
     * @param note The note to insert.
     */

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    /**
     * Updates an existing note in the database.
     *
     * @param note The note with updated values.
     */

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    /**
     * Deletes a note from the database.
     *
     * @param note The note to delete.
     */

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    /**
     * Retrieves a single note by its id.
     *
     * @param id The id of the note to retrieve.
     * @return The note if found, or null if it does not exist.
     */

    suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }
}