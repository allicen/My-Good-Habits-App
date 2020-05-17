package ru.application.habittracker.ui.habits.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_list.*
import ru.application.habittracker.*
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.GetHabitsListInterface
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.core.adapter.TabAdapter
import ru.application.habittracker.ui.habits.item.AddItemFragment
import java.io.Serializable
import kotlin.math.max


class ListFragment: Fragment(), Serializable {
    var position: Int = Constants.ITEM_POSITION_DEFAULT
    var deleteElem: Boolean = false
    var callback : GetHabitsListInterface? = null
    var orientationScreenOrActive: String = ""

    lateinit var oneItem: HabitItem

    lateinit var emptyList: LinearLayout
    lateinit var titleList: TextView
    lateinit var habitList: ArrayList<HabitItem>

    lateinit var tabs: LinearLayout
    lateinit var tabsLayout: TabLayout
    lateinit var tabsViewpager: ViewPager

    companion object {
        fun newInstance() : ListFragment {
            return ListFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as GetHabitsListInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        habitList = ArrayList()
        oneItem = Constants.EMPTY_ITEM

        println("#########onCreate List")

        val bundle = this.arguments
        if (bundle != null) {
            position = bundle.getInt("position",
                Constants.ITEM_POSITION_DEFAULT
            )
            deleteElem = bundle.getBoolean("delete", false)
            oneItem = bundle.getParcelable("item") ?: Constants.EMPTY_ITEM
            habitList = bundle.getParcelableArrayList("habitList") ?: ArrayList()

            // Добавление привычки в список
            habitList = callback?.updateHabitListFromFragmentData(oneItem, position, deleteElem) ?: ArrayList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        emptyList = view.findViewById(R.id.empty_list)
        titleList = view.findViewById(R.id.title_list)
        tabs = view.findViewById(R.id.tabs)
        tabsViewpager = view.findViewById(R.id.tabs_viewpager)
        tabsLayout = view.findViewById(R.id.tabs_layout)

        hideStartText(habitList.size)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение списка привычек
        habitList = callback?.getHabitsList() ?: ArrayList()

        // Количество привычек
        val goodHabitsCount = habitList.filter { it.type == Constants.TYPE_HABITS[0] }.count()
        val badHabitsCount = habitList.filter { it.type == Constants.TYPE_HABITS[1] }.count()
        val sizeList = max(goodHabitsCount, badHabitsCount)

        // Табы
        tabsViewpager.adapter = TabAdapter(
            childFragmentManager,
            goodHabitsCount,
            badHabitsCount
        )
        tabsLayout.setupWithViewPager(tabsViewpager)

        hideStartText(sizeList)

        fab.setOnClickListener {
            Log.e("tag", "Открыто окно создания привычки")

            if (add_item_form_land != null) {
                orientationScreenOrActive = "land"
            } else {
                orientationScreenOrActive = "add"
            }

            val bundle = Bundle()
            bundle.putString("orientationScreenOrActive", orientationScreenOrActive)
            bundle.putInt("positions",
                Constants.ITEM_POSITION_DEFAULT
            )

            val addItemFragment = AddItemFragment.newInstance()
            addItemFragment.arguments = bundle

            if (add_item_form_land != null) {

                if (childFragmentManager.findFragmentByTag("addItem") != null) {
                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.add_item_form_land, addItemFragment, "addItem")?.addToBackStack("list")?.commit()
                } else {
                    activity?.supportFragmentManager?.beginTransaction()?.add(R.id.add_item_form_land, addItemFragment, "addItem")?.addToBackStack("list")?.commit()
                }

                view.findViewById<FrameLayout>(R.id.add_item_form_land).visibility = View.VISIBLE

            } else {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.remove(this)
                    ?.replace(R.id.container_habits_fragment, addItemFragment, "addItem")
                    ?.addToBackStack("list")?.commit()
            }
        }
        oneItem = Constants.EMPTY_ITEM
    }


    /**
     * Показать/скрыть начальный текст
     * */
    fun hideStartText (habitListSize: Int) {
        if (habitListSize == 0) {
            emptyList.visibility = TextView.VISIBLE
            titleList.visibility = TextView.VISIBLE
            tabs.visibility = LinearLayout.GONE
        } else {
            emptyList.visibility = TextView.GONE
            titleList.visibility = TextView.GONE
            tabs.visibility = LinearLayout.VISIBLE
        }
    }
}