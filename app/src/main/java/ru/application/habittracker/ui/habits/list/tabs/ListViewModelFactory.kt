package ru.application.habittracker.ui.habits.list.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.application.habittracker.data.FeedDao

class ListViewModelFactory(private val dao: FeedDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}