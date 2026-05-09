package com.thejv04.noteapp.data

import androidx.room.TypeConverter
import com.thejv04.noteapp.model.ChecklistItem
import org.json.JSONArray
import org.json.JSONObject

class ChecklistConverter {

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