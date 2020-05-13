package ru.application.habittracker

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_container_habits.*
import kotlinx.android.synthetic.main.fragment_list.*


var orientationScreenOrActive: String = ""

class MainActivity : AppCompatActivity(), ListInterface, GetHabitsListInterface {
    var goodHabitsList: ArrayList<HabitItem> = ArrayList()
    var badHabitsList: ArrayList<HabitItem> = ArrayList()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Навигация
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.container_fragment)

        appBarConfiguration = AppBarConfiguration(setOf( // Боковая навигация
            R.id.nav_home, R.id.nav_about), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setUpBottomNav(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.container_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setUpBottomNav(navController: NavController) { // Навигация снизу
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    override fun onBackPressed() { // Обработка кнопки назад
        val lastFragments: FragmentManager = supportFragmentManager
        val fragmentByTag = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
        if (lastFragments.backStackEntryCount > 0 && (fragmentByTag == "main" && container_habits_fragment != null)) {
            lastFragments.popBackStack()
        } else {
            finish()
            startActivity(intent)
        }
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

        val arrayHabitList = when (data.type) { // Получить входящей привычки
            Constants.TYPE_HABITS[0] -> goodHabitsList
            else -> badHabitsList
        }

        if (position < arrayHabitList.size || changeType) {
            when {
                position >= 0 && delete && !changeType -> { // Удаление привычки
                    arrayHabitList.removeAt(position)
                }
                position >= 0 && !delete && !changeType-> { // Редактирование привычки
                    arrayHabitList[position] = data
                }
                position >= 0 && !delete && changeType-> { // Смена типа привычки (Сохранение в другой тип)
                    if (arrayHabitList == goodHabitsList) {
                        arrayHabitList.add(data)
                        badHabitsList.removeAt(position)
                    } else {
                        arrayHabitList.add(data)
                        goodHabitsList.removeAt(position)
                    }
                }
                else -> { // Добавление привычки
                    if (data != Constants.EMPTY_ITEM) {
                        arrayHabitList.add(data)
                    }
                }
            }
        }

        return arrayHabitList
    }


    /**
     * Получение списка хороших или плохих привычек
     * @param type Тип привычек
     * @return Список привычек с нужным типом
     * */

    override fun getGoodHabitsList(type: String): ArrayList<HabitItem> {
        return when (type) {
            "good" -> goodHabitsList
            else -> badHabitsList
        }
    }


    /**
     * Получение фрагментов после запуска activity
     * */
    fun getFragmentWithList() {
        val listFragment = ListFragment.newInstance()

        if (container_habits_fragment != null) {
            if (supportFragmentManager.findFragmentByTag("list") == null) {
                supportFragmentManager.beginTransaction().add(R.id.container_habits_fragment, listFragment, "list").addToBackStack("container").commitAllowingStateLoss()
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.container_habits_fragment, listFragment, "list").addToBackStack("container").commitAllowingStateLoss()
            }
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

            if (container_habits_fragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(listFragment).replace(R.id.container_habits_fragment, addItemFragment).addToBackStack("container").commitAllowingStateLoss()
            }

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