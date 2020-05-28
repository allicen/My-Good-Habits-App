package ru.application.habittracker.ui.habits.list.tabs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.data.Data

class TabsListViewModel: ViewModel() {
    private val habitsList : MutableLiveData<ArrayList<HabitItem>> = MutableLiveData()

    init {
        habitsList.value = Data.habitList
    }

    fun getListHabits() = habitsList
}