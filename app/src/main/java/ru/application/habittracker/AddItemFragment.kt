package ru.application.habittracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.add_item_fragment.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("########create Item")

        val bundle = this.arguments
        position = bundle?.getInt("position", Constants.ITEM_POSITION_DEFAULT) ?: Constants.ITEM_POSITION_DEFAULT
        changeItem = bundle?.getParcelable("changeItem") ?: Constants.EMPTY_ITEM
        orientationScreenOrActive = bundle?.getString("orientationScreenOrActive") ?: "land"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.add_item_fragment, container, false)

        headerTitle = view.findViewById(R.id.header_title)
        titleImage = view.findViewById(R.id.title_image)
        titleText = view.findViewById(R.id.title_text)
        burgerMenu = view.findViewById(R.id.menu_burger)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        when (orientationScreenOrActive) { // Заголовки
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

        itemType = good.text.toString() // Значение по умолчанию
        types = listOf(
            mapOf(bad to bad.text.toString()),
            mapOf(bad to bad.text.toString()))

        // радиокнопки
        good.setOnClickListener{ checkedRadioButton(it) }
        bad.setOnClickListener{ checkedRadioButton(it) }

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
            itemTitle = title_item.text.toString().trim()
            itemDescription = description_item.text.toString().trim()
            itemPriority = priority_item.selectedItem.toString()
            itemCount = count_item.text.toString()
            itemPeriod = period_item.text.toString().trim()

            if (itemTitle != "") {
                val item = HabitItem(
                    title = itemTitle,
                    description = itemDescription,
                    type = itemType,
                    priority = itemPriority,
                    count = itemCount,
                    period = itemPeriod)

                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putBoolean("delete", false)
                bundle.putParcelable("item", item)

                val listFragment = ListFragment()
                listFragment.arguments = bundle

                activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.list_activity, listFragment)?.addToBackStack("main")?.commit()

            } else {
                Snackbar.make(it, resources.getString(R.string.error_empty_title), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }


        // Удаление привычки
        delete.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("position", position)
            bundle.putBoolean("delete", true)

            val listFragment = ListFragment.newInstance(arrayListOf())
            listFragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.replace(R.id.list_activity, listFragment)?.addToBackStack("main")?.commit()
        }
    }

    /**
     * Метод для переключения радиокнопок
     * @param radioButton Радиокнопки
     * */
    private fun checkedRadioButton (radioButton: View) {
        for (type in types) {
            for ((key, value) in type) {
                if (key != radioButton) {
                    key.isChecked = false
                } else {
                    itemType = value
                }
            }
        }
    }
}