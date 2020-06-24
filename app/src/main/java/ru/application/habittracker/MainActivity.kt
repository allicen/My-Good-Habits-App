package ru.application.habittracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_container_habits.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.core.HabitListInterface
import ru.application.habittracker.core.HabitListUpdateInterface
import ru.application.habittracker.data.FeedDao
import ru.application.habittracker.ui.habits.filter.FilterResultFragment
import ru.application.habittracker.ui.habits.item.AddItemFragment
import ru.application.habittracker.ui.habits.list.ListFragment


class MainActivity : AppCompatActivity(), HabitListUpdateInterface,
    HabitListInterface {
    private lateinit var appBarConfiguration: AppBarConfiguration
    var orientationScreenOrActive: String = ""

    lateinit var dao: FeedDao

    override fun onCreate(savedInstanceState: Bundle?) {
        dao = getContextFromApp()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Навигация
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.activity_main)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.container_fragment)


        appBarConfiguration = AppBarConfiguration(setOf( // Боковая навигация
            R.id.habit_container, R.id.nav_about), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.container_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    private fun setUpBottomNav(navController: NavController) { // Навигация снизу
//        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
//        bottomNav?.setupWithNavController(navController)
//    }

    override fun onBackPressed() { // Обработка кнопки назад
        val lastFragments: FragmentManager = supportFragmentManager

        val fragmentByTag = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name

        println("back ********* $fragmentByTag")

        if (lastFragments.backStackEntryCount > 2) {
            lastFragments.popBackStack()
        } else {
            finish()
        }
    }


//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        outState.putParcelableArrayList("habitList", habitsList)
//    }
//
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//
//        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
//        habitsList = savedInstanceState.getParcelableArrayList("habitList")
//
//        getFragmentWithList()
//    }


    /**
     * Обновить данные в списке привычек
     * @param data Одна привычка
     * @param delete Метка удаления привычки
     * @return Обновленный список привычек
     * **/
    override fun updateHabitListFromFragmentData(data: HabitItem, delete: Boolean){
        if (data.type == "") {
            return
        }

        when {
            data.id != null && delete -> { // Удаление привычки
                GlobalScope.launch(Dispatchers.Default) {
                    dao.deleteById(data.id!!)
                }
            }
            data.id != null && !delete -> { // Редактирование привычки
                GlobalScope.launch(Dispatchers.Default) {
                    dao.update(data)
                }
            }
            else -> { // Добавление привычки
                if (data != Constants.EMPTY_ITEM) {
                    GlobalScope.launch(Dispatchers.Default) {
                        dao.insert(data)
                    }
                }
            }
        }
    }

    /**
     * Получение фрагментов после запуска activity
     * */
    override fun getFragmentWithList() {
        val listFragment = ListFragment.newInstance()

        if (container_habits_fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.container_habits_fragment, listFragment, "list").addToBackStack("container").commitAllowingStateLoss()
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

    override fun openListFragment(listFragment: ListFragment, addItemFragment: AddItemFragment) {
        supportFragmentManager.beginTransaction().remove(addItemFragment)
            .replace(R.id.container_habits_fragment, listFragment, "list").addToBackStack("addItem").commit()
    }

    override fun openAddItemFragment(addItemFragment: AddItemFragment) {
        if (add_item_form_land != null) {

            if (supportFragmentManager.findFragmentByTag("addItem") != null) {
                supportFragmentManager.beginTransaction().replace(R.id.add_item_form_land, addItemFragment, "addItem").addToBackStack("list").commit()
            } else {
                supportFragmentManager.beginTransaction().add(R.id.add_item_form_land, addItemFragment, "addItem").addToBackStack("list").commit()
            }

            findViewById<FrameLayout>(R.id.add_item_form_land).visibility = View.VISIBLE

        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_habits_fragment, addItemFragment, "addItem")
                .addToBackStack("list").commit()
        }
    }

    override fun openContainerFragment(listFragment: ListFragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container_habits_fragment, listFragment, "list")
            .addToBackStack("main").commitAllowingStateLoss()
    }

    override fun isLand(): Boolean {
        if (add_item_form_land == null) {
            return false
        }
        return true
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun getQueryFilter(query: String, habits: ArrayList<HabitItem>) {
        val tabs: LinearLayout = findViewById(R.id.tabs)

        tabs.visibility = View.GONE

        if (tab_layout_replace != null) {
            val filterResult = FilterResultFragment.newInstance(habits, query)
            supportFragmentManager.beginTransaction()
                .replace(R.id.tab_layout_replace, filterResult).addToBackStack("filter").commit()
        }
    }


    /**
     * Получение контекста, dao
     * **/
    override fun getContextFromApp(): FeedDao{
        val appClass: App = applicationContext as App
        return appClass.getDB().feedDao()
    }
}