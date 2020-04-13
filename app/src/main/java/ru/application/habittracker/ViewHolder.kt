package ru.application.habittracker

import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.*

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: Item){
        val title: EditText = itemView.findViewById(R.id.title_item)
        val description: EditText = itemView.findViewById(R.id.description_item)

        title.text = item.title_item as Editable?
        description.text = item.description_item as Editable?
    }
}