package ru.application.habittracker.core

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.application.habittracker.R

class RecHolder(itemView: View): RecyclerView.ViewHolder(itemView),
    HabitListUpdateInterface {

    fun bind(item: HabitItem, orientationScreenOrActive: String){

        val title: TextView = itemView.findViewById(R.id.item_title)
        val description: TextView = itemView.findViewById(R.id.item_description)
        val type: TextView = itemView.findViewById(R.id.item_type)
        val priority: TextView = itemView.findViewById(R.id.item_priority)
        val count: TextView = itemView.findViewById(R.id.item_count)
        val period: TextView = itemView.findViewById(R.id.period_item)
        val hash: TextView = itemView.findViewById(R.id.hash_item)

        title.text = item.title
        description.text = item.description
        type.text = item.type
        priority.text = item.priority
        count.text = item.count
        period.text = item.period

        itemView.setOnClickListener{
            val changeItem = HabitItem(
                id = item.id,
                title = item.title,
                description = item.description,
                type = item.type,
                priority = item.priority,
                count = item.count,
                period = item.period
            )

            println("###### changeItem ${changeItem.id}")

            updateHabitItem(itemView, changeItem, bindingAdapterPosition, orientationScreenOrActive)
        }
    }
}