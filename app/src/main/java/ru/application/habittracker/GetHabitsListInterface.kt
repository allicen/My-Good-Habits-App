package ru.application.habittracker

interface GetHabitsListInterface : ListInterface {
    fun updateHabitListFromFragmentData(data: HabitItem, position: Int, delete: Boolean = false, changeType: Boolean = false): ArrayList<HabitItem>
    fun getGoodHabitsList(type: String): ArrayList<HabitItem>
}