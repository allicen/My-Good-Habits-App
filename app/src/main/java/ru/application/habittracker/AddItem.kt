package ru.application.habittracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.add_item.*
import kotlin.collections.List


class AddItem: AppCompatActivity() {
    lateinit var types: List<Map<RadioButton, String>>

    lateinit var itemTitle: String
    lateinit var itemDescription: String
    lateinit var itemType: String
    lateinit var itemPriority: String
    lateinit var itemCount: String
    lateinit var itemPeriod: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        itemType = work.text.toString() // Значение по умолчанию
        types = listOf(
            mapOf(work to work.text.toString()),
            mapOf(study to study.text.toString()),
            mapOf(health to health.text.toString()),
            mapOf(family to family.text.toString()),
            mapOf(life to life.text.toString()))

        // радиокнопки
        work.setOnClickListener{ checkedRadioButton(it) }
        study.setOnClickListener{ checkedRadioButton(it) }
        health.setOnClickListener{ checkedRadioButton(it) }
        family.setOnClickListener{ checkedRadioButton(it) }
        life.setOnClickListener{ checkedRadioButton(it) }

        val position = intent.getIntExtra("position", -10)
        val changeItem = intent.getParcelableExtra<HabitItem>("changeItem")

        title_item.setText(changeItem.title, TextView.BufferType.EDITABLE)
        description_item.setText(changeItem.description, TextView.BufferType.EDITABLE)
        count_item.setText(changeItem.count, TextView.BufferType.EDITABLE)
        period_item.setText(changeItem.period, TextView.BufferType.EDITABLE)

        if (changeItem.priority != "") { // Выбор пункта спиннера по тексту
            for (i in 0 until priority_item.adapter.count) {
                if (priority_item.adapter.getItem(i).toString().contains(changeItem.priority)) {
                    priority_item.setSelection(i)
                }
            }
        }

        if (changeItem.type != "") { // Выбор типа привычки по тексту
            for (type in types) {
                for ((key, value) in type) {
                    key.isChecked = value == changeItem.type
                }
            }
        }

        if (position !=-1) {
            delete.visibility = TextView.VISIBLE
        }

        save.setOnClickListener {
            itemTitle = title_item.text.toString().trim()
            itemDescription = description_item.text.toString().trim()
            itemPriority = priority_item.selectedItem.toString()
            itemCount = count_item.text.toString()
            itemPeriod = period_item.text.toString().trim()

            if (itemTitle != "") {
                val item = HabitItem(itemTitle, itemDescription, itemType, itemPriority, itemCount, itemPeriod)

                val intent = Intent(this, List::class.java)
                    .apply {
                        putExtra("test", item)
                        putExtra("position", position)
                    }
                setResult(0, intent)
                finish()

            } else {
                Snackbar.make(it, resources.getString(R.string.error_empty_title), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }

        delete.setOnClickListener {
            val delete = Intent(this, List::class.java)
                .apply {
                    putExtra("position", position)
                }
            setResult(0, intent)
            finish()
        }
    }

    fun checkedRadioButton (radioButton: View) {

        for (type in types) {
            for ((key, value) in type) {
                if (key != radioButton) {
                    key.isChecked = false
                } else {
                    itemType = value
                }
            }
        }
    }
}