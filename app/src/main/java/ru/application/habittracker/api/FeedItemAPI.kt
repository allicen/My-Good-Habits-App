package ru.application.habittracker.api

import retrofit2.http.Field

class FeedItemAPI(
    val title: String,
    val description: String,
    val type: String,
    val priority: String,
    val count: String,
    val period: String
)