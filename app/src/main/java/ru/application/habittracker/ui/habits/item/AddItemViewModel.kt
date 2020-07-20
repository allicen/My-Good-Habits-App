package ru.application.habittracker.ui.habits.item

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.application.habittracker.api.NetworkController
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.data.FeedDao


class AddItemViewModel(private val dao: FeedDao) : ViewModel() {

    fun updateDB (delete: Boolean, update: Boolean, item: HabitItem){
        when {
            delete -> { // Удаление привычки
                delete(item)
            }
            update -> { // Редактирование привычки
                update(item)
            }
            else -> { // Добавление привычки
                insert(item)
            }
        }
    }

    private fun insert(habit: HabitItem) {
        if (habit != Constants.EMPTY_ITEM) {
            GlobalScope.launch(Dispatchers.Default) {
                //dao.insert(data)
            }
            NetworkController().netWorkPut(habit, dao)
        }
    }

    private fun update(habit: HabitItem) {
        GlobalScope.launch(Dispatchers.Default) {
            dao.update(habit)
        }
        NetworkController().netWorkPut(habit, dao)
    }


    private fun delete(habit: HabitItem) {
        GlobalScope.launch(Dispatchers.Default) {
            dao.deleteById(habit.id)
        }
        NetworkController().netWorkDelete(habit.id)
    }
}