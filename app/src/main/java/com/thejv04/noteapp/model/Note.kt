package com.thejv04.noteapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val color: Int = 0,
    val isChecklist: Boolean = false,
    val checklistItems: String = "",
    val imageUris: String = ""
)