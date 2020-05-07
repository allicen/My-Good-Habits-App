package ru.application.habittracker

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.list_fragment.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ListInterface {
    var habitList: ArrayList<HabitItem> = ArrayList()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        if (savedInstanceState == null) {
            getFragmentWithList()
        }

        // навигация
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.list_activity)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_about), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
        val listFragment = ListFragment.newInstance(habitList)

        if (supportFragmentManager.findFragmentByTag("list") == null) {
            supportFragmentManager.beginTransaction().add(R.id.list_activity, listFragment, "list").addToBackStack("main").commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.list_activity, listFragment, "list").addToBackStack("main").commitAllowingStateLoss()
        }
    }

    /**
     * Получить фрагмент с привычкой для формы редактирования
     * @param addItemFragment Одна привычка
     * */
    fun getFragmentWithUpdateItem(addItemFragment: AddItemFragment) {

        if (add_item_form_land == null) {
            val listFragment = ListFragment.newInstance(habitList)

            supportFragmentManager.beginTransaction()
                .remove(listFragment).replace(R.id.list_activity, addItemFragment).addToBackStack("main").commitAllowingStateLoss()
        } else {

            fab.visibility = View.GONE

            if (supportFragmentManager.findFragmentByTag("addItem") != null) {
                supportFragmentManager.beginTransaction().replace(R.id.add_item_form_land, addItemFragment, "addItem").addToBackStack("main").commit()
            } else {
                supportFragmentManager.beginTransaction().add(R.id.add_item_form_land, addItemFragment, "addItem").addToBackStack("main").commit()
            }

            @Suppress("PLUGIN_WARNING")
            add_item_form_land.visibility = View.VISIBLE
        }
    }
}