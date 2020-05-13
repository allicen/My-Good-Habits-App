package ru.application.habittracker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabAdapter (fragmentManager: FragmentManager, private val goodHabitsCount: Int, private val badHabitsCount: Int): FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> GoodHabitsFragment.newInstance()
            else -> BadHabitsFragment.newInstance()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Хорошие ($goodHabitsCount)"
            else -> "Плохие ($badHabitsCount)"
        }
    }
}