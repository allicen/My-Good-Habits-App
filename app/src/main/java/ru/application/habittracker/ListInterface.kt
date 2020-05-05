package ru.application.habittracker

import android.os.Bundle
import android.view.View

interface ListInterface {
    fun updateHabitItem(itemView: View, changeItem: HabitItem, adapterPosition: Int) {
        val bundle = Bundle()
        bundle.putInt("position", adapterPosition)
        bundle.putParcelable("changeItem", changeItem)

        val itemFragment = AddItemFragment()
        itemFragment.arguments = bundle

        (itemView.context as MainActivity).getFragmentWithUpdateItem(itemFragment)
    }

    fun updateHabitListFromFragmentData(data: HabitItem, position: Int, delete: Boolean = false): ArrayList<HabitItem>
}