package ru.application.habittracker.ui.habits.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.application.habittracker.core.HabitItem

class AddItemViewModel(private val item: HabitItem) : ViewModel() {

    private val _title = MutableLiveData<String>().apply { item.title }
    private val _description = MutableLiveData<String>().apply { item.description }
    private val _type = MutableLiveData<String>().apply { item.type }
    private val _priority = MutableLiveData<String>().apply { item.priority }
    private val _count = MutableLiveData<String>().apply { item.count }
    private val _period = MutableLiveData<String>().apply { item.period }
    private val _hash = MutableLiveData<Int>().apply { item.hash }

    val title: LiveData<String> = _title
    val description: LiveData<String> = _description
    val type: LiveData<String> = _type
    val priority: LiveData<String> = _priority
    val count: LiveData<String> = _count
    val period: LiveData<String> = _period
    val hash: LiveData<Int> = _hash

}