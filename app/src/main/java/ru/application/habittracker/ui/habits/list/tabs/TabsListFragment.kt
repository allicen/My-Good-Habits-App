package ru.application.habittracker.ui.habits.list.tabs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.application.habittracker.*
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.GetHabitsListInterface
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.core.adapter.RecAdapter
import ru.application.habittracker.data.Data

class TabsListFragment: Fragment() {
    var callback : GetHabitsListInterface? = null
    lateinit var habitsList: ArrayList<HabitItem>
    lateinit var goodHabits: ArrayList<HabitItem>
    lateinit var badHabits: ArrayList<HabitItem>
    lateinit var vRecViewHabitsList: RecyclerView
    lateinit var emptyListText: TextView
    var positionTab: Int = 0

    // Иницифлизация модели ленивым способом
    private val tabListViewModel by lazy { ViewModelProviders.of(this).get(TabsListViewModel::class.java) }

    companion object {
        fun newInstance(positionTab: Int, habitsList: ArrayList<HabitItem>): TabsListFragment {
            val bundle = Bundle()
            bundle.putInt("positionTab", positionTab)
            bundle.putParcelableArrayList("habitsList", habitsList)
            val fragment = TabsListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as GetHabitsListInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем список привычек и фильтруем его по типу
//        habitsList = Data.habitList

        arguments?.let {
            habitsList = it.getParcelableArrayList("habitsList") ?: ArrayList()
        }

        goodHabits = habitsList.filter { it.type == Constants.TYPE_HABITS[0] } as ArrayList<HabitItem>
        badHabits = habitsList.filter { it.type == Constants.TYPE_HABITS[1] } as ArrayList<HabitItem>

        val bundle = this.arguments
        if (bundle != null) {
            positionTab = bundle.getInt("positionTab", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_habits_tab, container, false)

        vRecViewHabitsList = view.findViewById(R.id.habits_list)
        emptyListText = view.findViewById(R.id.empty_list_habits)

        when (positionTab) { // Сортировка привычек по позиции таба
            0 -> {
                val adapter = RecAdapter(goodHabits, orientationScreenOrActive)
                vRecViewHabitsList.adapter = adapter

                //подписываем адаптер на получение списка
                tabListViewModel.getListHabits().observe(this, Observer {
                    it?.let { adapter.getActualList() }
                })

                emptyListText.text = Constants.TYPE_HABITS_EMPTY[0]
                if (goodHabits.size > 0) {
                    emptyListText.visibility = View.GONE
                }
            }
            else -> {
                val adapter = RecAdapter(badHabits, orientationScreenOrActive)
                vRecViewHabitsList.adapter = adapter

                //подписываем адаптер на получение списка
                tabListViewModel.getListHabits().observe(this, Observer {
                    it?.let { adapter.getActualList() }
                })

                emptyListText.text = Constants.TYPE_HABITS_EMPTY[1]
                if (badHabits.size > 0) {
                    emptyListText.visibility = View.GONE
                }
            }
        }

        vRecViewHabitsList.layoutManager = LinearLayoutManager(activity)

        return view
    }
}