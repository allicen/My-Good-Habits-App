package ru.application.habittracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
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


        work.setOnClickListener{ checkedRadioButton(it) }
        study.setOnClickListener{ checkedRadioButton(it) }
        health.setOnClickListener{ checkedRadioButton(it) }
        family.setOnClickListener{ checkedRadioButton(it) }
        life.setOnClickListener{ checkedRadioButton(it) }

        save.setOnClickListener {
            itemTitle = title_item.text.toString()
            itemDescription = description_item.text.toString()
            itemPriority = priority_item.selectedItem.toString()
            itemCount = count_item.text.toString()
            itemPeriod = period_item.text.toString()


            val item = HabitItem(itemTitle, itemDescription, itemType, itemPriority, itemCount, itemPeriod)

            val intent = Intent(this, List::class.java)
                .apply {
                    putExtra("test", item)
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

    override fun onStart() {
        super.onStart()
    }
}