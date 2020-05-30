package ru.application.habittracker.ui.habits.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import ru.application.habittracker.core.Constants
import ru.application.habittracker.R
import ru.application.habittracker.core.adapter.TabAdapter
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.core.HabitListInterface
import ru.application.habittracker.ui.habits.filter.FilterFragment
import java.io.Serializable
import kotlin.math.max


class ListFragment: Fragment(), Serializable {
    var position: Int = Constants.ITEM_POSITION_DEFAULT
    var deleteElem: Boolean = false
    var callback : HabitListInterface? = null

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
        callback = activity as HabitListInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oneItem = Constants.EMPTY_ITEM

        println("#########onCreate List")

        val bundle = this.arguments
        if (bundle != null) {
            val startActivity: String = bundle.getString("start", "")
            if (startActivity.isEmpty()) {
                position = bundle.getInt("position",
                    Constants.ITEM_POSITION_DEFAULT
                )
                deleteElem = bundle.getBoolean("delete", false)
                oneItem = bundle.getParcelable("item") ?: Constants.EMPTY_ITEM

                // Добавление привычки в список
                callback?.updateHabitListFromFragmentData(oneItem, deleteElem)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        emptyList = view.findViewById(R.id.empty_list)
        titleList = view.findViewById(R.id.title_list)
        tabs = view.findViewById(R.id.tabs)
        tabsViewpager = view.findViewById(R.id.tabs_viewpager)
        tabsLayout = view.findViewById(R.id.tabs_layout)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feed: LiveData<List<HabitItem>> = callback?.getContextFromApp()?.getAll()!!
        feed.observe(viewLifecycleOwner, Observer { feeds ->
            habitList = feeds as ArrayList<HabitItem>

            for (id in habitList) {
                println("===== ${id.id}")
            }

            // Количество привычек
            val goodHabitsCount = habitList.filter { it.type == Constants.TYPE_HABITS[0] }.count()
            val badHabitsCount = habitList.filter { it.type == Constants.TYPE_HABITS[1] }.count()
            val sizeList = max(goodHabitsCount, badHabitsCount)

            // Табы
            tabsViewpager.adapter = TabAdapter(
                childFragmentManager,
                goodHabitsCount,
                badHabitsCount, habitList
            )
            tabsLayout.setupWithViewPager(tabsViewpager)

            hideStartText(sizeList)
        })

        // Фрагмент для фильтра
        val filterFragment = FilterFragment.newInstance()
        if (childFragmentManager.findFragmentByTag("filter") == null) {
            childFragmentManager.beginTransaction().add(R.id.list_tab_fragment, filterFragment, "filter").addToBackStack("filter").commitAllowingStateLoss()
        } else {
            childFragmentManager.beginTransaction().replace(R.id.list_tab_fragment, filterFragment, "filter").addToBackStack("filter").commitAllowingStateLoss()
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