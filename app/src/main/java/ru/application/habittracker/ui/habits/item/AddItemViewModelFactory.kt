package ru.application.habittracker.ui.habits.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.application.habittracker.data.FeedDao

class AddItemViewModelFactory(private val dao: FeedDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddItemViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}