package com.thejv04.noteapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.thejv04.noteapp.model.Note

/**
 * Main database class for the application using Room.
 * Contains the [Note] entity and handles database migrations.
 *
 * Uses the Singleton pattern to ensure only one instance
 * of the database is created throughout the app lifecycle.
 */

@Database(entities = [Note::class], version = 3, exportSchema = false)
@TypeConverters(ChecklistConverter::class)
abstract class NoteDatabase : RoomDatabase() {

    /**
     * Provides access to the [NoteDao] for database operations.
     */

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        /**
         * Migration from version 1 to 2.
         * Adds [isChecklist] and [checklistItems] columns to the notes table.
         */

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE notes ADD COLUMN isChecklist INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE notes ADD COLUMN checklistItems TEXT NOT NULL DEFAULT ''")
            }
        }

        /**
         * Migration from version 2 to 3.
         * Adds [imageUris] column to the notes table.
         */

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE notes ADD COLUMN imageUris TEXT NOT NULL DEFAULT ''")
            }
        }

        /**
         * Returns the singleton instance of [NoteDatabase].
         * Creates the database if it does not exist yet.
         *
         * @param context The application context.
         * @return The singleton [NoteDatabase] instance.
         */
         
        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}