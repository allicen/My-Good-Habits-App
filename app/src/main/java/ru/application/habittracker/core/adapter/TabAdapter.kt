package ru.application.habittracker.core.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.application.habittracker.ui.habits.list.tabs.TabListFragment
import ru.application.habittracker.core.HabitItem

class TabAdapter (fragmentManager: FragmentManager,
                  private val goodHabitsCount: Int,
                  private val badHabitsCount: Int): FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return TabListFragment.newInstance(
            position
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