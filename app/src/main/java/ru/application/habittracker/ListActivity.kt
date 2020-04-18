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
    var position: Int = Constants.ITEM_POSITION_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        habitList = ArrayList()
        adapter = RecAdapter(habitList)

        val changeItem = HabitItem(
            title = "",
            description = "",
            type = "",
            period = "",
            count = "",
            priority = "")

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

    /**
     * Метод для работы с адаптером
     * @param items Список привычек
     * @param pos Позиция привычки для удаления
     * */
    private fun showItems(items: ArrayList<HabitItem> = arrayListOf(), pos: Int = position) {
        if (pos >= 0) {
            adapter.notifyItemRemoved(pos)
        }

        if (items.size > 0) {
            adapter = RecAdapter(items)
        }

        habit_list.adapter = adapter
        habit_list.layoutManager = LinearLayoutManager(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        position = data?.getIntExtra("position", Constants.ITEM_POSITION_DEFAULT) ?: Constants.ITEM_POSITION_DEFAULT
        val getHabit = data?.getParcelableExtra<HabitItem>("one_habit")

        if (getHabit != null) {
            if (position == Constants.ITEM_POSITION_DEFAULT) {
                habitList.add(getHabit)
            }
            else {
                if (position >= 0) {
                    habitList[position] = getHabit
                }
            }
            showItems()
        } else {
            if(position >= 0) {
                habitList.removeAt(position)
                showItems(pos = position)
            }
        }

        hideShowText()
        position = Constants.ITEM_POSITION_DEFAULT
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
        showItems(items = habitList)
        hideShowText()
    }

    private fun hideShowText() {
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