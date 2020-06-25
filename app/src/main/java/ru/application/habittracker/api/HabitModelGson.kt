package ru.application.habittracker.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.HashMap

class HabitModelGson(
    val title: String,
    val description: String,
    val type: String,
    val priority: String,
    val count: String,
    val period: String
): Serializable {

}