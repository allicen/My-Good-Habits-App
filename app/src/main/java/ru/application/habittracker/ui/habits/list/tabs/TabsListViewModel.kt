package ru.application.habittracker.ui.habits.list.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.application.habittracker.core.HabitItem

class TabsListViewModel: ViewModel() {
    private val habitsList_ : MutableLiveData<List<HabitItem>> = MutableLiveData()
    val habitsList: LiveData<List<HabitItem>> = habitsList_
}