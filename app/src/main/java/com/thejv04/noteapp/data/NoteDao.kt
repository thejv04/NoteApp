package com.thejv04.noteapp.data

import androidx.room.*
import com.thejv04.noteapp.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the notes database.
 * Provides methods to perform CRUD operations on the [Note] entity.
 */

@Dao
interface NoteDao {
    
    /**
     * Retrieves all notes ordered by timestamp in descending order.
     * Returns a [Flow] that emits a new list whenever the data changes.
     */
    
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    /**
     * Inserts a new note into the database.
     * If a note with the same id already exists, it will be replaced.
     *
     * @param note The note to insert.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    /**
     * Updates an existing note in the database.
     *
     * @param note The note with updated values.
     */

    @Update
    suspend fun updateNote(note: Note)

    /**
     * Deletes a note from the database.
     *
     * @param note The note to delete.
     */

    @Delete
    suspend fun deleteNote(note: Note)

    /**
     * Retrieves a single note by its id.
     *
     * @param id The id of the note to retrieve.
     * @return The note if found, or null if it does not exist.
     */

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?
}