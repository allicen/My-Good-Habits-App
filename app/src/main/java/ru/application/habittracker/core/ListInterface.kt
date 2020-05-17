package ru.application.habittracker.core

import android.os.Bundle
import android.view.View
import ru.application.habittracker.MainActivity
import ru.application.habittracker.ui.habits.item.AddItemFragment

interface ListInterface {
    fun updateHabitItem(
        itemView: View,
        changeItem: HabitItem,
        adapterPosition: Int,
        orientationScreenOrActive: String
    ) {

        val bundle = Bundle()
        bundle.putInt("position", adapterPosition)
        bundle.putParcelable("changeItem", changeItem)

        val itemFragment = AddItemFragment.newInstance()
        itemFragment.arguments = bundle

        (itemView.context as MainActivity).getFragmentWithUpdateItem(itemFragment, adapterPosition, changeItem)
    }
}