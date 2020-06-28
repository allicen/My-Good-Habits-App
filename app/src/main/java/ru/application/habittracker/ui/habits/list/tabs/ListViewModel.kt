package ru.application.habittracker.ui.habits.list.tabs

import androidx.lifecycle.*
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.data.FeedDao

class ListViewModel(dao: FeedDao): ViewModel() {
    val habitsList: LiveData<List<HabitItem>> = dao.getAll()

}