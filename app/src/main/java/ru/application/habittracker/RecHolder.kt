@file:Suppress("CAST_NEVER_SUCCEEDS")

package ru.application.habittracker

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: HabitItem){
        val title: TextView = itemView.findViewById(R.id.item_title)
        val description: TextView = itemView.findViewById(R.id.item_description)
        val type: TextView = itemView.findViewById(R.id.item_type)
        val priority: TextView = itemView.findViewById(R.id.item_priority)
        val count: TextView = itemView.findViewById(R.id.item_count)
        val period: TextView = itemView.findViewById(R.id.period_item)


        title.text = item.title
        description.text = item.description
        type.text = item.type
        priority.text = item.priority
        count.text = item.count
        period.text = item.period

        itemView.setOnClickListener{
            val changeItem = HabitItem(item.title, item.description, item.type, item.priority, item.count, item.period)

            val changeHabit = Intent(itemView.context, AddItem::class.java)
                .apply {
                    putExtra("changeItem", changeItem)
                    putExtra("position", adapterPosition)
                }

            (itemView.context as Activity).startActivityForResult(changeHabit, 0)
        }
    }
}