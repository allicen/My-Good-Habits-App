package ru.application.habittracker

class Constants {
    companion object{
        const val ITEM_POSITION_DEFAULT = -10
        val EMPTY_ITEM = HabitItem(
            title = "",
            description = "",
            type = "",
            period = "",
            count = "",
            priority = "")
    }
}