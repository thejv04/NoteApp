package com.thejv04.noteapp.model

/**
 * Represents a single item in a checklist note.
 *
 * @property id Unique identifier generated using UUID.
 * @property text Text content of the checklist item.
 * @property isChecked Whether the item has been checked off.
 */

data class ChecklistItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String = "",
    val isChecked: Boolean = false
)