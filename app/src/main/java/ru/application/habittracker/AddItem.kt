package ru.application.habittracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_item.*
import kotlin.collections.List


class AddItem: AppCompatActivity() {
    lateinit var types: List<RadioButton>

    lateinit var itemTitle: String
    lateinit var itemDescription: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        types = listOf<RadioButton>(work, study, health, family, life)

        work.setOnClickListener{ checkedRadioButton(it) }
        study.setOnClickListener{ checkedRadioButton(it) }
        health.setOnClickListener{ checkedRadioButton(it) }
        family.setOnClickListener{ checkedRadioButton(it) }
        life.setOnClickListener{ checkedRadioButton(it) }

        save.setOnClickListener {
            itemTitle = title_item.text.toString()
            itemDescription = description_item.text.toString()

            val item = HabitItem(itemTitle, itemDescription)

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
            if (type != radioButton) {
                type.isChecked = false
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }
}