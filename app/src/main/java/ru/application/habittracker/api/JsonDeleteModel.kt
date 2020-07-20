package ru.application.habittracker.api

import com.google.gson.annotations.SerializedName

data class JsonDeleteModel (
    @SerializedName("uid") val uid: String
)