package ru.application.habittracker.core.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.application.habittracker.ui.habits.list.tabs.TabsListFragment
import ru.application.habittracker.core.HabitItem

class TabAdapter (fragmentManager: FragmentManager,
                  private val goodHabitsCount: Int,
                  private val badHabitsCount: Int, private val habitsList: ArrayList<HabitItem>): FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return TabsListFragment.newInstance(
            position,
            habitsList
        )
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Хорошие ($goodHabitsCount)"
            else -> "Плохие ($badHabitsCount)"
        }
    }
}