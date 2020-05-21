package ru.application.habittracker.core.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.R
import ru.application.habittracker.core.RecHolder
import java.io.Serializable

class RecAdapter(private val items: ArrayList<HabitItem>, private val orientationScreenOrActive: String): RecyclerView.Adapter<RecHolder>(), Serializable {
    lateinit var blockItem: LinearLayout
    lateinit var priorityItem: TextView
    lateinit var countItem: TextView
    lateinit var periodItem: TextView
    lateinit var delimItem: TextView
    lateinit var descriptionItem: TextView
    lateinit var reply: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.item, parent, false)

        blockItem = view.findViewById(R.id.one_item)
        priorityItem = view.findViewById(R.id.item_priority)
        descriptionItem = view.findViewById(R.id.item_description)
        countItem = view.findViewById(R.id.item_count)
        periodItem = view.findViewById(R.id.period_item)
        delimItem = view.findViewById(R.id.delim)
        reply = view.findViewById(R.id.reply)

        return RecHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getActualList() {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {
        val item = items[position]

        if (item.count == "" || item.period == "") { // Скрыть разделитель
            delimItem.visibility =View.GONE
            countItem.visibility =View.GONE
            periodItem.visibility =View.GONE
            reply.visibility =View.GONE
        }

        if (item.description == "") { // Скрыть пустое описание
            descriptionItem.visibility = View.GONE
        }

        if (item.priority == Constants.TYPE_PRIORITY[0]) { // Установить цвет приоритета
            priorityItem.setBackgroundColor(Color.parseColor("#f8f183"))
        } else if (item.priority == Constants.TYPE_PRIORITY[1]) {
            priorityItem.setBackgroundColor(Color.parseColor("#f8c283"))
        } else {
            priorityItem.setBackgroundColor(Color.parseColor("#ea661e"))
            priorityItem.setTextColor(Color.parseColor("#ffffff"))
        }

        if (item.type == Constants.TYPE_HABITS[0]) { // Перекрасить рамку в зависимости от типа привычки
            blockItem.setBackgroundResource(R.drawable.good_habit_item)
        } else {
            blockItem.setBackgroundResource(R.drawable.bad_habit_item)
        }

        holder.bind(item, orientationScreenOrActive)
    }
}