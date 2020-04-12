package ru.application.habittracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.list.*

import kotlinx.android.synthetic.main.main.*

class List : AppCompatActivity() {

    var habitCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
//        setSupportActionBar(toolbar)

        if (habitCount == 0) {
            empty_list.visibility = TextView.VISIBLE
        }

        fab.setOnClickListener {
//                view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()

            Log.e("tag", "Открыто окно создания привычки")
            val addHabit = Intent(this, Item::class.java)
            startActivity(addHabit)
        }
    }
}