package ru.application.habittracker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabAdapter (fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> {
            ListFragment.newInstance(arrayListOf())
        }
        else -> {
            ListFragment.newInstance(arrayListOf())
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return "${position + 1} fragment"
    }
}