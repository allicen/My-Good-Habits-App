package ru.application.habittracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class RecAdapter(private val items: ArrayList<HabitItem>, private val orientationScreenOrActive: String): RecyclerView.Adapter<RecHolder>(), Serializable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.item, parent, false)

        return RecHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {
        val item = items[position]
        holder.bind(item, orientationScreenOrActive)
    }
}