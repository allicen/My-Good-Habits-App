package ru.application.habittracker

class Constants {
    companion object{
        const val ITEM_POSITION_DEFAULT = -10
        val TYPE_HABITS = listOf("Хорошие", "Плохие")
        val TYPE_HABITS_EMPTY = listOf("Хорошие привычки не добавлены", "Плохие привычки не добавлены")
        val TYPE_PRIORITY = listOf("Не важно", "Важно", "Очень важно")

        val EMPTY_ITEM = HabitItem(
            title = "",
            description = "",
            type = "",
            period = "",
            count = "",
            priority = "",
            hash = 0)

        var hash = 0
    }
}