package ru.application.habittracker.core

import ru.application.habittracker.data.FeedDao
import ru.application.habittracker.ui.habits.list.ListFragment
import ru.application.habittracker.ui.habits.item.AddItemFragment

interface HabitListInterface : HabitListUpdateInterface {
//    fun updateHabitListFromFragmentData(data: HabitItem, delete: Boolean = false, update: Boolean = false)
    fun openListFragment(listFragment: ListFragment, addItemFragment: AddItemFragment)
    fun openAddItemFragment(addItemFragment: AddItemFragment)
    fun openContainerFragment(listFragment: ListFragment)
    fun getFragmentWithList()
    fun isLand(): Boolean
    fun getQueryFilter(query: String, habits: ArrayList<HabitItem>)

    fun getContextFromApp(): FeedDao
}