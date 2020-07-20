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
import ru.application.habittracker.core.Constants
import ru.application.habittracker.R
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.core.HabitListInterface
import ru.application.habittracker.core.adapter.RecAdapter
import ru.application.habittracker.data.FeedDao

class TabListFragment: Fragment() {
    var callback : HabitListInterface? = null
    lateinit var habitsList: ArrayList<HabitItem>
    lateinit var goodHabits: ArrayList<HabitItem>
    lateinit var badHabits: ArrayList<HabitItem>
    lateinit var vRecViewHabitsList: RecyclerView
    lateinit var emptyListText: TextView
    var positionTab: Int = 0

    var orientationScreenOrActive: String = ""

    private lateinit var viewModel: ListViewModel
    lateinit var dao: FeedDao

    companion object {
        fun newInstance(positionTab: Int, habitsList: ArrayList<HabitItem>): TabListFragment {
            val bundle = Bundle()
            bundle.putInt("positionTab", positionTab)
            bundle.putParcelableArrayList("habitsList", habitsList)
            val fragment = TabListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as HabitListInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = this.arguments
        if (bundle != null) {
            positionTab = bundle.getInt("positionTab", 0)
        }



        dao = callback?.getContextFromApp()!!
        viewModel = requireActivity().run {
            ViewModelProviders.of(this,
                ListViewModelFactory(dao)
            ).get(ListViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_habits_tab, container, false)

        vRecViewHabitsList = view.findViewById(R.id.habits_list)
        emptyListText = view.findViewById(R.id.empty_list_habits)

        viewModel.habitsList.observe(this, Observer { feeds ->
            habitsList = feeds as ArrayList<HabitItem>

            goodHabits = habitsList.filter { it.type == 0 } as ArrayList<HabitItem>
            badHabits = habitsList.filter { it.type == 1 } as ArrayList<HabitItem>


            when (positionTab) { // Сортировка привычек по позиции таба
                0 -> {
                    val adapter =
                        RecAdapter(
                            goodHabits,
                            orientationScreenOrActive
                        )
                    vRecViewHabitsList.adapter = adapter

                    emptyListText.text = Constants.TYPE_HABITS_EMPTY[0]
                    if (goodHabits.size > 0) {
                        emptyListText.visibility = View.GONE
                    }
                }
                else -> {
                    val adapter =
                        RecAdapter(
                            badHabits,
                            orientationScreenOrActive
                        )
                    vRecViewHabitsList.adapter = adapter

                    emptyListText.text = Constants.TYPE_HABITS_EMPTY[1]
                    if (badHabits.size > 0) {
                        emptyListText.visibility = View.GONE
                    }
                }
            }

            vRecViewHabitsList.layoutManager = LinearLayoutManager(activity)
        })

        return view
    }
}