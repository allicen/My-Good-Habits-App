package ru.application.habittracker.core

import ru.application.habittracker.ui.habits.list.ListFragment
import ru.application.habittracker.ui.habits.item.AddItemFragment

interface HabitListInterface : HabitListUpdateInterface {
    fun updateHabitListFromFragmentData(data: HabitItem, position: Int, delete: Boolean = false): ArrayList<HabitItem>
    fun openListFragment(listFragment: ListFragment, addItemFragment: AddItemFragment)
    fun openAddItemFragment(addItemFragment: AddItemFragment)
    fun openContainerFragment(listFragment: ListFragment)
    fun getFragmentWithList()
    fun isLand(): Boolean
    fun getQueryFilter(query: String)
    fun getHabitList():  ArrayList<HabitItem>
}