package ru.application.habittracker.ui.habits.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.application.habittracker.core.Constants

class AddItemViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddItemViewModel(item = Constants.EMPTY_ITEM) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}