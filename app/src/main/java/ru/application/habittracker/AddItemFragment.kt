package ru.application.habittracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_item.*


class AddItemFragment: Fragment() {

    companion object {
        fun newInstance() : AddItemFragment {
            return AddItemFragment()
        }
    }

    lateinit var types: List<Map<RadioButton, String>>
    lateinit var orientationScreenOrActive: String

    lateinit var itemTitle: String
    lateinit var itemDescription: String
    lateinit var itemType: String
    lateinit var itemPriority: String
    lateinit var itemCount: String
    lateinit var itemPeriod: String
    lateinit var headerTitle: LinearLayout
    lateinit var titleImage: ImageView
    lateinit var titleText: TextView
    lateinit var burgerMenu: ImageView

    var position: Int = Constants.ITEM_POSITION_DEFAULT
    lateinit var changeItem: HabitItem
    var hash: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("########create Item")

        val bundle = this.arguments
        position = bundle?.getInt("position", Constants.ITEM_POSITION_DEFAULT) ?: Constants.ITEM_POSITION_DEFAULT
        changeItem = bundle?.getParcelable("changeItem") ?: Constants.EMPTY_ITEM
        hash = changeItem.hash
        orientationScreenOrActive = bundle?.getString("orientationScreenOrActive") ?: "land"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)

        headerTitle = view.findViewById(R.id.header_title)
        titleImage = view.findViewById(R.id.title_image)
        titleText = view.findViewById(R.id.title_text)
        burgerMenu = view.findViewById(R.id.menu_burger)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        when (orientationScreenOrActive) { // Заголовки окна
            "edit" -> {
                titleText.text = "Редактировать привычку"
            }
            "add" -> {
                titleText.text = "Добавить привычку"
            }
            else -> {
                headerTitle.visibility = View.GONE
            }
        }

        itemType = ""
        type_habit.setOnCheckedChangeListener{ _, _ -> // Радиокнопки
            if (good.isChecked) {
                itemType = Constants.TYPE_HABITS[0]
            } else {
                itemType = Constants.TYPE_HABITS[1]
            }
        }

        title_item.setText(changeItem.title, TextView.BufferType.EDITABLE)
        description_item.setText(changeItem.description, TextView.BufferType.EDITABLE)
        count_item.setText(changeItem.count, TextView.BufferType.EDITABLE)
        period_item.setText(changeItem.period, TextView.BufferType.EDITABLE)

        if (changeItem.priority != "") { // Выбор пункта спиннера по тексту
            for (i in 0 until priority_item.adapter.count) {
                if (priority_item.adapter.getItem(i).toString().contains(changeItem.priority)) {
                    priority_item.setSelection(i)
                }
            }
        }

        types = listOf( // Типы радиокнопок
            mapOf(good to good.text.toString()),
            mapOf(bad to bad.text.toString()))

        if (changeItem.type != "") { // Выбор типа привычки по тексту
            for (type in types) {
                for ((key, value) in type) {
                    key.isChecked = value == changeItem.type
                }
            }
        }

        // Скрыть/Показать ссылку для удаления привычки
        if (position != Constants.ITEM_POSITION_DEFAULT) {
            delete.visibility = TextView.VISIBLE
        }

        // Сохранение привычки
        save.setOnClickListener {
            pushData (false)
        }

        // Удаление привычки
        delete.setOnClickListener {
            pushData (true)
        }

    }


    /**
     * Метод для получения одной привычки
     * @return Привычка с полями
     * */

    fun getItem (): HabitItem {
        itemTitle = title_item.text.toString().trim()
        itemDescription = description_item.text.toString().trim()
        itemPriority = priority_item.selectedItem.toString()
        itemCount = count_item.text.toString()
        itemPeriod = period_item.text.toString().trim()

        if (hash == 0) {
            hash = Constants.hash + 1
            Constants.hash = hash
        }

        return HabitItem(
            title = itemTitle,
            description = itemDescription,
            type = itemType,
            priority = itemPriority,
            count = itemCount,
            period = itemPeriod,
            hash = hash)
    }


    /**
     * Метод для сохранения и удаления привычки
     * @param delete Удалить или нет запись
     * */

    fun pushData (delete: Boolean) {
        val item: HabitItem = getItem ()

        if (item.title != "") {
            if (item.type == "") {
                Snackbar.make(view!!, resources.getString(R.string.error_empty_type), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putBoolean("delete", delete)
                bundle.putParcelable("item", item)

                val listFragment = ListFragment.newInstance()
                listFragment.arguments = bundle

                activity?.supportFragmentManager?.beginTransaction()?.remove(this)
                    ?.replace(R.id.container_habits_fragment, listFragment, "list")?.addToBackStack("addItem")?.commit()
            }
        } else {
            Snackbar.make(view!!, resources.getString(R.string.error_empty_title), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}