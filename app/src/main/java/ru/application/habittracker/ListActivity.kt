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

class ListActivity : AppCompatActivity() {

    lateinit var habitList: ArrayList<HabitItem>
    lateinit var adapter: RecAdapter
    var position: Int = -10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        habitList = ArrayList()
        adapter = RecAdapter(habitList)

        val changeItem = HabitItem("", "", "", "", "", "")

        fab.setOnClickListener {
            Log.e("tag", "Открыто окно создания привычки")
            val addHabit = Intent(this, AddItemActivity::class.java)
                .apply {
                    putExtra("changeItem", changeItem)
                    putExtra("position", position)
                }
            startActivityForResult(addHabit, 0)
        }

        println("######### onCreate")
        empty_list.visibility = TextView.VISIBLE
    }

    fun showItems(items: ArrayList<HabitItem>) {
        /// надо адаптер определить выше и переопределять тут adapter.set() и остальные методы также использовать тут
        adapter = RecAdapter(items)
        habit_list.adapter = adapter
        habit_list.layoutManager = LinearLayoutManager(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        position = data?.getIntExtra("position", -1) ?: -10
        val getHabit = data?.getParcelableExtra<HabitItem>("test")

        if (getHabit != null) {
            if (position == -10) {
                habitList.add(getHabit)
            }
            else {
                habitList[position] = getHabit
            }
        } else {
            if(position >= 0) {
                habitList.removeAt(position) // Криво работает. Удаляет при возврате назад
            }
        }

        hideShowText()
        showItems(habitList)

        println("######### onActivityResult")
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

        hideShowText()
    }

    fun hideShowText() {
        if (habitList.isEmpty()) {
            empty_list.visibility = TextView.VISIBLE
            title_list.visibility = TextView.VISIBLE
        } else {
            empty_list.visibility = TextView.GONE
            title_list.visibility = TextView.GONE
        }
    }

    override fun onResume() {
        println("######### onResume")
        super.onResume()
    }

    override fun onStart() {
        println("######### onStart")
        super.onStart()
    }

    override fun onPause() {
        println("######### onPause")
        super.onPause()
    }

    override fun onDestroy() {
        println("######### onDestroy")
        super.onDestroy()
    }
}