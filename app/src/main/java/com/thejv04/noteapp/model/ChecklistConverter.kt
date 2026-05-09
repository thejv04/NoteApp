package com.thejv04.noteapp.data

import androidx.room.TypeConverter
import com.thejv04.noteapp.model.ChecklistItem
import org.json.JSONArray
import org.json.JSONObject

/**
 * Room [TypeConverter] for converting a list of [ChecklistItem] objects
 * to and from a JSON string for database storage.
 *
 * Since Room cannot store complex objects directly, this converter
 * serializes the list into a JSON string and deserializes it back
 * when reading from the database.
 */

class ChecklistConverter {

    /**
     * Converts a list of [ChecklistItem] objects into a JSON string.
     * Used by Room when writing to the database.
     *
     * @param items The list of checklist items to serialize.
     * @return A JSON string representation of the list.
     */

    @TypeConverter
    fun fromChecklistItems(items: List<ChecklistItem>): String {
        val array = JSONArray()
        items.forEach { item ->
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("text", item.text)
            obj.put("isChecked", item.isChecked)
            array.put(obj)
        }
        return array.toString()
    }

    /**
     * Converts a JSON string into a list of [ChecklistItem] objects.
     * Used by Room when reading from the database.
     *
     * @param json The JSON string to deserialize.
     * @return A list of [ChecklistItem] objects, or an empty list if the string is blank.
     */

    @TypeConverter
    fun toChecklistItems(json: String): List<ChecklistItem> {
        if (json.isEmpty()) return emptyList()
        val array = JSONArray(json)
        val items = mutableListOf<ChecklistItem>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            items.add(
                ChecklistItem(
                    id = obj.getString("id"),
                    text = obj.getString("text"),
                    isChecked = obj.getBoolean("isChecked")
                )
            )
        }
        return items
    }
}