package ru.application.habittracker

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.android.synthetic.main.list_activity.*
import java.util.*
import kotlin.collections.ArrayList

class ListActivity : AppCompatActivity(), ListInterface {
    var habitList: ArrayList<HabitItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        if (savedInstanceState == null) {
            getFragmentWithList()
        }
    }

    override fun updateHabitListFromFragmentData(data: HabitItem):ArrayList<HabitItem> {
        habitList.add(data)
        return habitList
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList("array", habitList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        habitList = savedInstanceState.getParcelableArrayList("array")
        getFragmentWithList()
    }

    fun getFragmentWithList() {
        val fragment = ListFragment.newInstance(habitList)
        supportFragmentManager.beginTransaction().add(R.id.list_activity, fragment, "list").commitAllowingStateLoss()
    }
}