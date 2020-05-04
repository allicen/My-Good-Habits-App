package ru.application.habittracker

import android.app.Activity
import android.content.Intent
import android.view.View

interface ListInterface {
    fun getIntent(itemView: View, changeItem: HabitItem, adapterPosition: Int) {
        val changeHabit = Intent(itemView.context, AddItemActivity::class.java)
            .apply {
                putExtra("changeItem", changeItem)
                putExtra("position", adapterPosition)
            }

        (itemView.context as Activity).startActivityForResult(changeHabit, 0)
    }

    fun updateHabitListFromFragmentData(data: HabitItem): ArrayList<HabitItem>
}