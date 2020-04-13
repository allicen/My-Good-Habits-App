package ru.application.habittracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.item.*
import kotlinx.android.synthetic.main.list.*
import kotlin.collections.List


class Item: AppCompatActivity() {
    lateinit var types: List<RadioButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item)

        types = listOf<RadioButton>(work, study, health, family, life)

        work.setOnClickListener{ checkedRadioButton(it) }
        study.setOnClickListener{ checkedRadioButton(it) }
        health.setOnClickListener{ checkedRadioButton(it) }
        family.setOnClickListener{ checkedRadioButton(it) }
        life.setOnClickListener{ checkedRadioButton(it) }

        save.setOnClickListener {
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