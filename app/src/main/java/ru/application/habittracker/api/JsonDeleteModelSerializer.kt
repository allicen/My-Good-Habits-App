package ru.application.habittracker.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class JsonDeleteModelSerializer: JsonSerializer<JsonDeleteModel> {
    override fun serialize(src: JsonDeleteModel?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement = JsonObject().apply {
        addProperty("uid", src?.uid)
    }
}