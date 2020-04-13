package ru.application.habittracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.list.*
import kotlinx.android.synthetic.main.main.*

class List : AppCompatActivity() {

    lateinit var habitList: MutableList<Item>
    private var habitCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        if (habitCount == 0) {
            empty_list.visibility = TextView.VISIBLE
        }

        fab.setOnClickListener {
            Log.e("tag", "Открыто окно создания привычки")
            val addHabit = Intent(this, Item::class.java)
            startActivity(addHabit)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            println(data)
        }
    }
}