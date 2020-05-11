package ru.application.habittracker

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_list.*
import kotlin.collections.ArrayList
var orientationScreenOrActive: String = ""

class MainActivity : AppCompatActivity(), ListInterface, GetHabitsListInterface {
    var goodHabitsList: ArrayList<HabitItem> = ArrayList()
    var badHabitsList: ArrayList<HabitItem> = ArrayList()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // навигация
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.container_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_about, R.id.nav_test), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.container_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList("goodHabitsList", goodHabitsList)

        outState.putParcelableArrayList("badHabitsList", badHabitsList)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        goodHabitsList = savedInstanceState.getParcelableArrayList("goodHabitsList")
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        badHabitsList = savedInstanceState.getParcelableArrayList("badHabitsList")

        getFragmentWithList()
    }


    /**
     * Обновить данные в списке привычек
     * @param data Одна привычка
     * @param position Позиция привычки
     * @param delete Метка удаления привычки
     * @return Обновленный список привычек
     * **/
    override fun updateHabitListFromFragmentData(data: HabitItem, position: Int, delete: Boolean, changeType: Boolean): ArrayList<HabitItem> {
        if (data.type == "") {
            return ArrayList()
        }

        val arrayHabitList = when (data.type) {
            Constants.TYPE_HABITS[0] -> goodHabitsList
            else -> badHabitsList
        }

        if (position < arrayHabitList.size || changeType) {
            when {
                position >= 0 && delete && !changeType -> {
                    arrayHabitList.removeAt(position)
                }
                position >= 0 && !delete && !changeType-> {
                    arrayHabitList[position] = data
                }
                position >= 0 && !delete && changeType-> {
                    if (arrayHabitList == goodHabitsList) {
                        arrayHabitList.add(data)
                        badHabitsList.removeAt(position)
                    } else {
                        arrayHabitList.add(data)
                        goodHabitsList.removeAt(position)
                    }
                }
                else -> {
                    if (data != Constants.EMPTY_ITEM) {
                        arrayHabitList.add(data)
                    }
                }
            }
        }

        return arrayHabitList
    }

    override fun getGoodHabitsList(type: String): ArrayList<HabitItem> {
        return when (type) {
            "good" -> goodHabitsList
            else -> badHabitsList
        }
    }


    /**
     * Работа с фрагментами
     * */
    fun getFragmentWithList() {
        val listFragment = ListFragment.newInstance()

        if (supportFragmentManager.findFragmentByTag("list") == null) {
            supportFragmentManager.beginTransaction().add(R.id.container_habits_fragment, listFragment, "list").addToBackStack("main").commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.container_habits_fragment, listFragment, "list").addToBackStack("main").commitAllowingStateLoss()
        }
    }

    /**
     * Получить фрагмент с привычкой для формы редактирования
     * @param addItemFragment Одна привычка
     * */
    fun getFragmentWithUpdateItem(addItemFragment: AddItemFragment, adapterPosition: Int, changeItem: HabitItem) {

        if (add_item_form_land != null) {
            orientationScreenOrActive = "land"
        } else {
            orientationScreenOrActive = "edit"
        }

        val bundle = Bundle()
        bundle.putString("orientationScreenOrActive", orientationScreenOrActive)
        bundle.putInt("position", adapterPosition)
        bundle.putParcelable("changeItem", changeItem)
        addItemFragment.arguments = bundle

        if (add_item_form_land == null) {
            val listFragment = ListFragment.newInstance()

            supportFragmentManager.beginTransaction()
                .remove(listFragment).replace(R.id.container_habits_fragment, addItemFragment).addToBackStack("main").commitAllowingStateLoss()
        } else {

            if (supportFragmentManager.findFragmentByTag("addItem") != null) {
                supportFragmentManager.beginTransaction().add(R.id.add_item_form_land, addItemFragment, "addItem").addToBackStack("main").commit()
            } else {
                supportFragmentManager.beginTransaction().add(R.id.add_item_form_land, addItemFragment, "addItem").addToBackStack("main").commit()
            }

            @Suppress("PLUGIN_WARNING")
            findViewById<FrameLayout>(R.id.add_item_form_land).visibility = View.VISIBLE
        }
    }
}