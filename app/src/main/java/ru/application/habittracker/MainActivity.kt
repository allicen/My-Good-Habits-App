package ru.application.habittracker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.list_fragment.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ListInterface {
    var habitList: ArrayList<HabitItem> = ArrayList()
    var emptyItem: HabitItem = Constants.EMPTY_ITEM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        if (savedInstanceState == null) {
            getFragmentWithList()
        }
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


    /**
     * Обновить данные в списке привычек
     * @param data Одна привычка
     * @param position Позиция привычки
     * @param delete Метка удаления привычки
     * @return Обновленный список привычек
     * **/
    override fun updateHabitListFromFragmentData(data: HabitItem, position: Int, delete: Boolean): ArrayList<HabitItem> {

        if (position < habitList.size) {
            when {
                position >= 0 && delete -> {
                    habitList.removeAt(position)
                }
                position >= 0 && !delete -> {
                    habitList[position] = data
                }
                else -> {
                    habitList.add(data)
                }
            }
        }

        return habitList
    }

    /**
     * Работа с фрагментами
     * */
    fun getFragmentWithList() {
        val fragment = ListFragment.newInstance(habitList)
        supportFragmentManager.beginTransaction().add(R.id.list_activity, fragment, "list").commitAllowingStateLoss()
    }

    /**
     * Получить фрагмент с привычкой для формы редактирования
     * @param addItemFragment Одна привычка
     * */
    fun getFragmentWithUpdateItem(addItemFragment: AddItemFragment) {

        if (add_item_form_land == null) {
            val listFragment = supportFragmentManager.findFragmentByTag("list") as ListFragment

            supportFragmentManager.beginTransaction()
                .hide(listFragment).replace(R.id.list_activity, addItemFragment).addToBackStack("main").commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.add_item_form_land, addItemFragment, "addItem").addToBackStack(null).commit()

            @Suppress("PLUGIN_WARNING")
            add_item_form_land.visibility = View.VISIBLE
        }
    }
}