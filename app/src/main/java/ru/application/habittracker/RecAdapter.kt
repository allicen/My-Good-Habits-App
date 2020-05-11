package ru.application.habittracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors.getColor
import java.io.Serializable

class RecAdapter(private val items: ArrayList<HabitItem>, private val orientationScreenOrActive: String): RecyclerView.Adapter<RecHolder>(), Serializable {
    lateinit var blockItem: LinearLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.item, parent, false)

        blockItem = view.findViewById(R.id.one_item)

        return RecHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {
        val item = items[position]
        if (item.type == Constants.TYPE_HABITS[0]) {
            blockItem.setBackgroundColor(Color.parseColor("#c8fec8"))
        } else {
            blockItem.setBackgroundColor(Color.parseColor("#fedbc8"))
        }

        holder.bind(item, orientationScreenOrActive)
    }
}