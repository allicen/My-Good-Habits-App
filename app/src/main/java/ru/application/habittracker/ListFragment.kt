package ru.application.habittracker

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
import kotlinx.android.synthetic.main.list_fragment.*
import java.io.Serializable


class ListFragment: Fragment(), Serializable {
    var position: Int = Constants.ITEM_POSITION_DEFAULT
    var deleteElem: Boolean = false
    var callback : ListInterface? = null

    lateinit var oneItem: HabitItem

    lateinit var vRecView: RecyclerView
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

        vRecView.adapter = RecAdapter(habitList)
        vRecView.layoutManager = LinearLayoutManager(activity)

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

        @Suppress("PLUGIN_WARNING")
        fab.setOnClickListener {
            Log.e("tag", "Открыто окно создания привычки")

            val addItemFragment = AddItemFragment.newInstance()

            if (add_item_form_land != null) {
                @Suppress("PLUGIN_WARNING")
                add_item_form_land.visibility = View.VISIBLE

            } else {
                val listFragment = activity?.supportFragmentManager?.findFragmentByTag("list") as ListFragment

                activity?.supportFragmentManager?.beginTransaction()
                    ?.hide(listFragment)?.replace(R.id.list_activity, addItemFragment)?.addToBackStack("main")?.commit()
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