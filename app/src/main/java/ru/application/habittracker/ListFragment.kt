package ru.application.habittracker

import android.R.attr.button
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.list_fragment.*
import java.io.Serializable


class ListFragment: Fragment(), Serializable {
    var position: Int = Constants.ITEM_POSITION_DEFAULT
    var deleteElem: Boolean = false
    var callback : ListInterface? = null

    lateinit var oneItem: HabitItem

    lateinit var vRecView: RecyclerView
    lateinit var listTabsViewpager: ViewPager
    lateinit var listTabsLayout: TabLayout
    lateinit var emptyList: LinearLayout
    lateinit var titleList: TextView
    lateinit var habitList: ArrayList<HabitItem>

    companion object {
        fun newInstance(habitList: ArrayList<HabitItem>) : ListFragment {
            val fragment = ListFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("habitList", habitList)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as ListInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        habitList = ArrayList()
        oneItem = Constants.EMPTY_ITEM

        val bundle = this.arguments
        if (bundle != null) {
            position = bundle.getInt("position", 0)
            deleteElem = bundle.getBoolean("delete", false)
            oneItem = bundle.getParcelable("item") ?: Constants.EMPTY_ITEM
            habitList = bundle.getParcelableArrayList("habitList") ?: ArrayList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        vRecView = view.findViewById(R.id.habit_list)
        emptyList = view.findViewById(R.id.empty_list)
        titleList = view.findViewById(R.id.title_list)
//        listTabsLayout = view.findViewById(R.id.list_tabs_layout)
//        listTabsViewpager = view.findViewById(R.id.list_tabs_viewpager)

        vRecView.adapter = RecAdapter(habitList) // основной список
        vRecView.layoutManager = LinearLayoutManager(activity)

//        listTabsViewpager.adapter = TabAdapter(childFragmentManager) // табы
//        listTabsLayout.setupWithViewPager(listTabsViewpager)

        hideStartText(habitList.size)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            if (oneItem.title != "" || deleteElem) {
                val data = callback?.updateHabitListFromFragmentData(oneItem, position, deleteElem)
                hideStartText(data?.size ?: 0)
                data?.let { showItems(it) }
            }
        }

        fab.setOnClickListener {
            Log.e("tag", "Открыто окно создания привычки")

            val addItemFragment = AddItemFragment.newInstance()

            if (add_item_form_land != null) {
                fab.visibility = View.GONE

                if (activity?.supportFragmentManager?.findFragmentByTag("addItem") != null) {
                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.add_item_form_land, addItemFragment, "addItem")?.addToBackStack("main")?.commit()
                } else {
                    activity?.supportFragmentManager?.beginTransaction()?.add(R.id.add_item_form_land, addItemFragment, "addItem")?.addToBackStack("main")?.commit()
                }

                @Suppress("PLUGIN_WARNING")
                add_item_form_land.visibility = View.VISIBLE

            } else {

                activity?.supportFragmentManager?.beginTransaction()?.remove(this)
                    ?.replace(R.id.list_activity, addItemFragment)?.addToBackStack("main")?.commit()
            }
        }
    }

    /**
     * Показать/скрыть начальный текст
     * */

    fun hideStartText (habitListSize: Int) {
        if (habitListSize == 0) {
            emptyList.visibility = TextView.VISIBLE
            titleList.visibility = TextView.VISIBLE
        } else {
            emptyList.visibility = TextView.GONE
            titleList.visibility = TextView.GONE
        }
    }


    /**
     * Метод для работы с адаптером
     * @param items Список привычек
     * @param pos Позиция привычки для удаления
     * */
    private fun showItems(items: ArrayList<HabitItem>, pos: Int = position) {

        if (!isVisible) {
            return
        }

        if (pos >= 0 && !deleteElem) {
            vRecView.adapter?.notifyItemChanged(pos)
        }

        if (pos >= 0 && deleteElem) {
            vRecView.adapter?.notifyItemRemoved(pos)
        }

        if (items.size > 0) {
            vRecView.adapter = RecAdapter(items)
        }

        vRecView.layoutManager = LinearLayoutManager(activity)
    }
}