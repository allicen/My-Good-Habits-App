package ru.application.habittracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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