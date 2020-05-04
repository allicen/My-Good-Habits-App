package ru.application.habittracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
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

    lateinit var itemTitle: String
    lateinit var itemDescription: String
    lateinit var itemType: String
    lateinit var itemPriority: String
    lateinit var itemCount: String
    lateinit var itemPeriod: String

    var position: Int = 0
    lateinit var changeItem: HabitItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("########create Item")

        val bundle = this.arguments
        position = bundle?.getInt("position", 0) ?: 0
        changeItem = bundle?.getParcelable("changeItem") ?: HabitItem(
            title = "",
            description = "",
            type = "",
            period = "",
            count = "",
            priority = "")

        println("######$position")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.add_item_fragment, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

        if (position != Constants.ITEM_POSITION_DEFAULT) {
            delete.visibility = TextView.VISIBLE
        }

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
                bundle.putParcelable("item", item)
                bundle.putString("mark", "fragment")
                val listFragment = ListFragment()
                listFragment.arguments = bundle

                //println("****** args ${listFragment}")

                activity?.supportFragmentManager?.beginTransaction()
                    ?.hide(this)?.replace(R.id.list_activity, listFragment)?.addToBackStack("main")?.commit()

            } else {
                Snackbar.make(it, resources.getString(R.string.error_empty_title), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
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