package ru.application.habittracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GoodHabitsFragment: Fragment() {
    var callback : GetHabitsListInterface? = null
    lateinit var goodHabitsList: ArrayList<HabitItem>
    lateinit var vRecViewHabitsList: RecyclerView

    companion object {
        fun newInstance(): GoodHabitsFragment {
            return GoodHabitsFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as GetHabitsListInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goodHabitsList = callback?.getGoodHabitsList("good") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_good_habits, container, false)

        vRecViewHabitsList = view.findViewById(R.id.good_habits_list)

        vRecViewHabitsList.adapter = RecAdapter(goodHabitsList, orientationScreenOrActive)
        vRecViewHabitsList.layoutManager = LinearLayoutManager(activity)

        return view
    }
}