package ru.application.habittracker

interface GetHabitsListInterface : ListInterface {
    fun updateHabitListFromFragmentData(data: HabitItem, position: Int, delete: Boolean = false): ArrayList<HabitItem>
    fun getHabitsList(): ArrayList<HabitItem>
}