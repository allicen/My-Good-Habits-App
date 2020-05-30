package ru.application.habittracker.ui.habits.item

import android.content.Context
import android.os.Bundle
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
import kotlinx.android.synthetic.main.item.*
import ru.application.habittracker.*
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.core.HabitListInterface
import ru.application.habittracker.ui.habits.list.ListFragment


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
    lateinit var itemPriority: String
    lateinit var itemCount: String
    lateinit var itemPeriod: String

    lateinit var headerTitle: LinearLayout
    lateinit var titleImage: ImageView
    lateinit var titleText: TextView
    lateinit var burgerMenu: ImageView

    lateinit var changeItem: HabitItem
    var itemId: Int? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as HabitListInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получение модели
        addItemViewModel = requireActivity().run {
            ViewModelProviders.of(this,
                AddItemViewModelFactory()
            ).get(AddItemViewModel::class.java)
        }

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
        if (itemId != null) {
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

        return HabitItem(
            id = itemId,
            title = itemTitle,
            description = itemDescription,
            type = itemType,
            priority = itemPriority,
            count = itemCount,
            period = itemPeriod
        )
    }


    /**
     * Метод для сохранения и удаления привычки
     * @param delete Удалить или нет запись
     * */

    fun pushData (delete: Boolean) {
        val item: HabitItem = getItem ()

        // Получение данных из модели
        addItemViewModel.title.observe(this, Observer { itemTitle = it })
        addItemViewModel.description.observe(this, Observer { itemDescription = it })
        addItemViewModel.type.observe(this, Observer { itemType = it })
        addItemViewModel.priority.observe(this, Observer { itemPriority = it })
        addItemViewModel.count.observe(this, Observer { itemCount = it })
        addItemViewModel.period.observe(this, Observer { itemPeriod = it })

        if (item.title != "") {
            if (item.type == "") {
                Snackbar.make(view!!, resources.getString(R.string.error_empty_type), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                val bundle = Bundle()
                bundle.putBoolean("delete", delete)
                bundle.putParcelable("item", item)

                val listFragment =
                    ListFragment.newInstance()
                listFragment.arguments = bundle

                callback?.openListFragment(listFragment, this)
            }
        } else {
            Snackbar.make(view!!, resources.getString(R.string.error_empty_title), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}