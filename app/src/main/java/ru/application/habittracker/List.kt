package ru.application.habittracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.list.*
import kotlinx.android.synthetic.main.main.*
import java.util.ArrayList

class List : AppCompatActivity() {

    lateinit var habitList: ArrayList<String>
    var emptyList = true

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

    fun showItems(item: String) {
        val infLater = layoutInflater

        val view = infLater.inflate(R.layout.item, list_item, false)
        val vTitle = view.findViewById<TextView>(R.id.item_title)
        vTitle.text = item
        list_item.addView(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val string = data?.getStringExtra("test").toString()

        if (string.isNotEmpty()) {
            habitList.add(string)
            empty_list.visibility = TextView.GONE
            showItems(string)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArrayList("array", habitList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        habitList = savedInstanceState.getStringArrayList("array")
        for (item in habitList) {
            showItems(item)
        }

        if (habitList.size != 0) {
            empty_list.visibility = TextView.GONE
        }
    }
}