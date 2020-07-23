package ru.application.habittracker.core

class Constants {
    companion object{
        const val ITEM_POSITION_DEFAULT = -10
        val TYPE_HABITS = listOf("Хорошие", "Плохие")
        val TYPE_HABITS_EMPTY = listOf("Хорошие привычки не добавлены", "Плохие привычки не добавлены")
        val TYPE_PRIORITY = listOf("Не важно", "Важно", "Очень важно")

        val EMPTY_ITEM = HabitItem(
            id = "",
            title = "",
            description = "",
            type = 0,
            period = "",
            count = 0,
            priority = 0
        )

        var hash = 0

        val URL_NETWORK = "https://droid-test-server.doubletapp.ru/api/"

        var query = ""
    }
}