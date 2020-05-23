package ru.application.habittracker.ui.habits.filter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.application.habittracker.R
import ru.application.habittracker.core.GetHabitsListInterface
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.core.adapter.RecAdapter
import ru.application.habittracker.orientationScreenOrActive
import ru.application.habittracker.ui.habits.list.tabs.TabsListFragment
import ru.application.habittracker.ui.habits.list.tabs.TabsListViewModel

class FilterResultFragment: Fragment() {

    lateinit var vRecViewHabitsList: RecyclerView
    lateinit var  titlePage: TextView
    lateinit var  filterResultHint: TextView
    lateinit var resetFilterResults: LinearLayout

    lateinit var habitsList: ArrayList<HabitItem>
    var query: String = ""

    var callback : GetHabitsListInterface? = null

    // Иницифлизация модели ленивым способом
    private val tabListViewModel by lazy { ViewModelProviders.of(this).get(TabsListViewModel::class.java) }

    companion object{
        fun newInstance(habitsList: ArrayList<HabitItem>, query: String): FilterResultFragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList("habitsList", habitsList)
            bundle.putString("query",query)
            val fragment = FilterResultFragment()
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

        arguments?.let {
            habitsList = it.getParcelableArrayList("habitsList") ?: ArrayList()
            query = it.getString("query") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_filter_result, container, false)

        titlePage = view.findViewById(R.id.filter_results_title)
        filterResultHint = view.findViewById(R.id.filter_results_hint)
        resetFilterResults = view.findViewById(R.id.reset_filter_results)

        vRecViewHabitsList = view.findViewById(R.id.habits_list)

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titlePage.text = "Поиск по запросу «$query»"

        if (habitsList.size == 0) {
            filterResultHint.text = "К сожалению, ничего не нашлось"
            resetFilterResults.visibility = View.GONE
        } else {
            filterResultHint.text = "Найдено результатов: ${habitsList.size}"
        }

        resetFilterResults.setOnClickListener {
            callback?.getFragmentWithList()
        }

        val adapter = RecAdapter(habitsList, orientationScreenOrActive)
        vRecViewHabitsList.adapter = adapter

        //подписываем адаптер на получение списка
        tabListViewModel.getListHabits().observe(this, Observer {
            it?.let { adapter.getActualList() }
        })

        vRecViewHabitsList.layoutManager = LinearLayoutManager(activity)
    }
}