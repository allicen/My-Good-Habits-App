package ru.application.habittracker.core

interface GetHabitsListInterface : ListInterface {
    fun updateHabitListFromFragmentData(data: HabitItem, position: Int, delete: Boolean = false): ArrayList<HabitItem>
    fun getHabitsList(): ArrayList<HabitItem>
}