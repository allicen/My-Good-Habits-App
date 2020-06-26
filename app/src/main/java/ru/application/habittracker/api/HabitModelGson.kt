package ru.application.habittracker.api

class HabitModelGson(
    val title: String,
    val description: String,
    val type: String,
    val priority: String,
    val count: String,
    val period: String,
    val color: Int,
    val date: Int,
    val frequency: Int,
    val uid: String
)