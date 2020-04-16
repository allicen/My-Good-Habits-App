package ru.application.habittracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.list.*
import kotlinx.android.synthetic.main.main.*
import java.util.ArrayList

class List : AppCompatActivity() {

    lateinit var habitList: ArrayList<HabitItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        habitList = ArrayList()

        fab.setOnClickListener {
            Log.e("tag", "Открыто окно создания привычки")
            val addHabit = Intent(this, AddItem::class.java)
                .apply {
                    putExtra("test", "")
                }
            startActivityForResult(addHabit, 0)
        }

        empty_list.visibility = TextView.VISIBLE
    }

    override fun onResume() {
        super.onResume()
    }

    fun showItems(items: ArrayList<HabitItem>) {
        val adapter = RecAdapter(items)
        habit_list.adapter = adapter
        habit_list.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val getHabit = data?.getParcelableExtra<HabitItem>("test")
        if (getHabit != null) {
            habitList.add(getHabit)
            empty_list.visibility = TextView.GONE
            title_list.visibility = TextView.GONE
        }
        showItems(habitList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList("array", habitList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        habitList = savedInstanceState.getParcelableArrayList("array")
        showItems(habitList)

        if (habitList.size != 0) {
            empty_list.visibility = TextView.GONE
            title_list.visibility = TextView.GONE
        }
    }
}