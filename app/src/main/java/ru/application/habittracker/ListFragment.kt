package ru.application.habittracker

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
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_fragment.*
import java.io.Serializable


class ListFragment: Fragment(), Serializable {
    var position: Int = Constants.ITEM_POSITION_DEFAULT
    var deleteElem: Boolean = false
    var callback : ListInterface? = null
    var orientationScreenOrActive: String = ""

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

        println("#########onCreate List")

        val bundle = this.arguments
        if (bundle != null) {
            position = bundle.getInt("position", Constants.ITEM_POSITION_DEFAULT)
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

        vRecView.adapter = RecAdapter(habitList, orientationScreenOrActive) // основной список
        vRecView.layoutManager = LinearLayoutManager(activity)

        hideStartText(habitList.size)

//        val data = callback?.updateHabitListFromFragmentData(Constants.EMPTY_ITEM, position, deleteElem)
//        hideStartText(data?.size ?: 0)
//        data?.let { showItems(it) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Добавление списка привычек на экран
        val data = callback?.updateHabitListFromFragmentData(oneItem, position, deleteElem)
        hideStartText(data?.size ?: 0)
        data?.let { showItems(it) }

        fab.setOnClickListener {
            Log.e("tag", "Открыто окно создания привычки")

            if (add_item_form_land != null) {
                orientationScreenOrActive = "land"
            } else {
                orientationScreenOrActive = "add"
            }

            val bundle = Bundle()
            bundle.putString("orientationScreenOrActive", orientationScreenOrActive)
            bundle.putInt("positions", Constants.ITEM_POSITION_DEFAULT)

            val addItemFragment = AddItemFragment.newInstance()
            addItemFragment.arguments = bundle

            if (add_item_form_land != null) {

                if (childFragmentManager.findFragmentByTag("addItem") != null) {
                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.add_item_form_land, addItemFragment, "addItem")?.addToBackStack("main")?.commit()
                } else {
                    activity?.supportFragmentManager?.beginTransaction()?.add(R.id.add_item_form_land, addItemFragment, "addItem")?.addToBackStack("main")?.commit()
                }

                view.findViewById<FrameLayout>(R.id.add_item_form_land).visibility = View.VISIBLE

            } else {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.remove(this)
                    ?.replace(R.id.container_habits_fragment, addItemFragment, "addItem")
                    ?.addToBackStack("main")?.commit()
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

        if (add_item_form_land == null) {
            orientationScreenOrActive = "edit"
        } else {
            orientationScreenOrActive = "land"
        }

        if (pos >= 0 && !deleteElem) {
            vRecView.adapter?.notifyItemChanged(pos)
        }

        if (pos >= 0 && deleteElem) {
            vRecView.adapter?.notifyItemRemoved(pos)
        }

        if (items.size > 0) {
            vRecView.adapter = RecAdapter(items, orientationScreenOrActive)
        }

        vRecView.layoutManager = LinearLayoutManager(activity)
    }
}