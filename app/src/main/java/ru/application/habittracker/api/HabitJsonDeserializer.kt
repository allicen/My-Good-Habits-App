package ru.application.habittracker.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.application.habittracker.core.HabitItem
import java.lang.reflect.Type

class HabitJsonDeserializer: JsonDeserializer<HabitItem> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): HabitItem = HabitItem(
        json.asJsonObject.get("id").asInt,
        json.asJsonObject.get("title").asString,
        json.asJsonObject.get("description").asString,
        json.asJsonObject.get("type").asString,
        json.asJsonObject.get("priority").asString,
        json.asJsonObject.get("count").asString,
        json.asJsonObject.get("period").asString
    )
}