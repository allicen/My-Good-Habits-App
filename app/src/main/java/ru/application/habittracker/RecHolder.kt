@file:Suppress("CAST_NEVER_SUCCEEDS")

package ru.application.habittracker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: HabitItem){
        val title: TextView = itemView.findViewById(R.id.item_title)
        val description: TextView = itemView.findViewById(R.id.item_description)

        title.text = item.title
        description.text = item.description

    }
}