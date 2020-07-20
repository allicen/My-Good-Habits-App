package ru.application.habittracker.ui.habits.item

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.fragment_add_item.period_item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.application.habittracker.*
import ru.application.habittracker.api.NetworkController
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.core.HabitListInterface
import ru.application.habittracker.data.FeedDao
import ru.application.habittracker.ui.habits.list.ListFragment
import java.util.*


class AddItemFragment: Fragment() {

    companion object {
        fun newInstance() : AddItemFragment {
            return AddItemFragment()
        }
    }

    // Модель
    private lateinit var addItemViewModel: AddItemViewModel

    // Связь с активити
    var callback : HabitListInterface? = null

    lateinit var types: List<Map<RadioButton, String>>
    lateinit var orientationScreenOrActive: String

    lateinit var itemTitle: String
    lateinit var itemDescription: String
    lateinit var itemType: String
    var typeInt: Int = -1
    lateinit var itemPriority: String
    var priorityInt: Int = -1
    lateinit var itemCount: String
    lateinit var itemPeriod: String

    lateinit var headerTitle: LinearLayout
    lateinit var titleImage: ImageView
    lateinit var titleText: TextView
    lateinit var burgerMenu: ImageView

    lateinit var changeItem: HabitItem
    var itemId: String = ""
    var update: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as HabitListInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("########create Item")

        val bundle = this.arguments
        changeItem = bundle?.getParcelable("changeItem") ?: Constants.EMPTY_ITEM
        orientationScreenOrActive = bundle?.getString("orientationScreenOrActive") ?: "land"

        itemId = changeItem.id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)

//        itemId = view.findViewById(R.id.hash_item)
        headerTitle = view.findViewById(R.id.header_title)
        titleImage = view.findViewById(R.id.title_image)
        titleText = view.findViewById(R.id.title_text)
        burgerMenu = view.findViewById(R.id.menu_burger)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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
                typeInt = 0
            } else {
                itemType = Constants.TYPE_HABITS[1]
                typeInt = 1
            }
        }

        title_item.setText(changeItem.title, TextView.BufferType.EDITABLE)
        description_item.setText(changeItem.description, TextView.BufferType.EDITABLE)
        count_item.setText(changeItem.count.toString(), TextView.BufferType.EDITABLE)
        period_item.setText(changeItem.period, TextView.BufferType.EDITABLE)

        if (changeItem.priority > -1) { // Выбор пункта спиннера по тексту
            for (i in 0 until priority_item.adapter.count) {
                if (priority_item.adapter.getItem(i).toString().contains(Constants.TYPE_PRIORITY[changeItem.priority])) {
                    priority_item.setSelection(i)
                    priorityInt = i
                }
            }
        }

        types = listOf( // Типы радиокнопок
            mapOf(good to good.text.toString()),
            mapOf(bad to bad.text.toString()))

        for (type in types) { // Выбор типа привычки по тексту
            for ((key, value) in type) {
                key.isChecked = value == Constants.TYPE_HABITS[changeItem.type]
            }
        }

        // Скрыть/Показать ссылку для удаления привычки
        if (itemId != "") {
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

        if (itemId == "") {
            //itemId = UUID.randomUUID().toString()
        } else {
            update = true
        }

        itemTitle = title_item.text.toString().trim()
        itemDescription = description_item.text.toString().trim()
        itemPriority = priority_item.selectedItem.toString()
        itemCount = count_item.text.toString()
        itemPeriod = period_item.text.toString().trim()

        // индекс приоритета привычки
        for (index in Constants.TYPE_PRIORITY.indices) {
            if (Constants.TYPE_PRIORITY[index] == itemPriority) {
                priorityInt = index
            }
        }

        return HabitItem(
            id = itemId,
            title = itemTitle,
            description = itemDescription,
            type = typeInt,
            priority = priorityInt,
            count = itemCount.toInt(),
            period = itemPeriod
        )
    }


    /**
     * Метод для сохранения и удаления привычки
     * @param delete Удалить или нет запись
     * */

    fun pushData (delete: Boolean) {
        val item: HabitItem = getItem ()

        // Получение модели
        val dao: FeedDao = callback?.getContextFromApp()!!
        addItemViewModel = requireActivity().run {
            ViewModelProviders.of(this,
                AddItemViewModelFactory(dao)
            ).get(AddItemViewModel::class.java)
        }

        addItemViewModel.updateDB (delete, update, item)


        if (item.title != "") {
            val listFragment =
                ListFragment.newInstance()

            callback?.openListFragment(listFragment, this)
        } else {
            Snackbar.make(view!!, resources.getString(R.string.error_empty_title), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}