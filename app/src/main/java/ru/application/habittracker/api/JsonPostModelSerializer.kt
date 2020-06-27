package ru.application.habittracker.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class JsonPostModelSerializer: JsonSerializer<JsonPostModel> {
    override fun serialize(src: JsonPostModel?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement = JsonObject().apply  {
        addProperty("date", src?.date)
        addProperty("habit_uid", src?.habit_uid)
    }
}