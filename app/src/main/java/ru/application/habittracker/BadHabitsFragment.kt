package ru.application.habittracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BadHabitsFragment: Fragment() {
    var callback : GetHabitsListInterface? = null
    lateinit var badHabitsList: ArrayList<HabitItem>
    lateinit var vRecViewHabitsList: RecyclerView
    lateinit var emptyListText: TextView

    companion object {
        fun newInstance(): BadHabitsFragment {
            return BadHabitsFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as GetHabitsListInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        badHabitsList = callback?.getGoodHabitsList("bad") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_bad_habits, container, false)

        vRecViewHabitsList = view.findViewById(R.id.bad_habits_list)
        emptyListText = view.findViewById(R.id.empty_list_bad_habits)

        if (badHabitsList.size > 0) {
            emptyListText.visibility = View.GONE
        }

        vRecViewHabitsList.adapter = RecAdapter(badHabitsList, orientationScreenOrActive)
        vRecViewHabitsList.layoutManager = LinearLayoutManager(activity)

        return view
    }
}