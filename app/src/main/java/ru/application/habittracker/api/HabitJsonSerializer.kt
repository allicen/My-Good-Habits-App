package ru.application.habittracker.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.application.habittracker.core.HabitItem
import java.lang.reflect.Type

class HabitJsonSerializer(val date: Long): JsonSerializer<HabitItem> {
    override fun serialize(src: HabitItem?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement = JsonObject().apply {
        addProperty("uid", src?.id)
        addProperty("title", src?.title)
        addProperty("description", src?.description)
        addProperty("type", src?.type)
        addProperty("priority", src?.priority)
        addProperty("count", src?.count)
        addProperty("date", date)
        addProperty("frequency", 0)
    }
}