package ru.application.habittracker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecHolder(itemView: View): RecyclerView.ViewHolder(itemView), ListInterface {

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
        hash.text = item.hash.toString()

        itemView.setOnClickListener{
            val changeItem = HabitItem(
                title = item.title,
                description = item.description,
                type = item.type,
                priority = item.priority,
                count = item.count,
                period = item.period,
                hash = item.hash)

            updateHabitItem(itemView, changeItem, bindingAdapterPosition, orientationScreenOrActive)
        }
    }
}