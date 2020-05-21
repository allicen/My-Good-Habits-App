package ru.application.habittracker

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.bottom_sheet_fragment.*
import kotlinx.android.synthetic.main.fragment_container_habits.*
import kotlinx.android.synthetic.main.fragment_list.*
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.GetHabitsListInterface
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.core.ListInterface
import ru.application.habittracker.data.Data
import ru.application.habittracker.ui.habits.ContainerHabitsFragment
import ru.application.habittracker.ui.habits.item.AddItemFragment
import ru.application.habittracker.ui.habits.list.ListFragment


var orientationScreenOrActive: String = ""

class MainActivity : AppCompatActivity(), ListInterface,
    GetHabitsListInterface {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomSheetShow: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val containerFragment = ContainerHabitsFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment_frame, containerFragment).addToBackStack("container").commitAllowingStateLoss()

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

        // Нижняя панель фильтра
        bottomSheetShow = findViewById<LinearLayout>(R.id.bottom_sheet_layout)
        val bottomSheetShow = findViewById<FrameLayout>(R.id.bottom_sheet) // нижняя видимая панель
        val bottomSheetText = findViewById<TextView>(R.id.show_bottom_panel) // Текст
        val bottomSheetImg = findViewById<ImageView>(R.id.show_bottom_panel_arrow) // Иконка
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout) // Вся панель
        bottomSheetShow.setOnClickListener {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetText.text = "Скрыть фильтр"
                bottomSheetImg.setImageResource(R.drawable.ic_keyboard_arrow_down)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                bottomSheetText.text = "Открыть фильтр"
                bottomSheetImg.setImageResource(R.drawable.ic_keyboard_arrow_up)
            }
        }
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

        // Показать нижнюю панель
        val bottomSheetShow = findViewById<LinearLayout>(R.id.bottom_sheet_layout)
        bottomSheetShow?.visibility = View.VISIBLE

        if (lastFragments.backStackEntryCount > 2) {
            lastFragments.popBackStack()
        } else {
            finish()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList("habitList", Data.habitList)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        Data.habitList = savedInstanceState.getParcelableArrayList("habitList")

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
        if (data.type == "") {
            return ArrayList()
        }

        if (position < Data.habitList.size) {
            when {
                position >= 0 && delete -> { // Удаление привычки
                    for ((index, element) in Data.habitList.withIndex()) {
                        if (element.hash == data.hash) {
                            Data.habitList.removeAt(index)
                            break
                        }
                    }
                }
                position >= 0 && !delete -> { // Редактирование привычки
                    for ((index, element) in Data.habitList.withIndex()) {
                        if (element.hash == data.hash) {
                            Data.habitList[index] = data
                            break
                        }
                    }
                }
                else -> { // Добавление привычки
                    val habitsHash = mutableListOf<Int>() // Hash добавленных привычек
                    for (habit in Data.habitList) {
                        habitsHash.add(habit.hash)
                    }

                    if (data != Constants.EMPTY_ITEM && data.hash !in habitsHash) {
                        Data.habitList.add(data)
                    }
                }
            }
        }

        return Data.habitList
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

    override fun showBottomSheet () {
        bottomSheetShow.visibility = View.VISIBLE
    }

    override fun hideBottomSheet () {
        bottomSheetShow.visibility = View.GONE
    }
}