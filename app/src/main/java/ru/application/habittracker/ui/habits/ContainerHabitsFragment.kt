package ru.application.habittracker.ui.habits

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.application.habittracker.core.HabitListInterface
import ru.application.habittracker.ui.habits.list.ListFragment
import ru.application.habittracker.R
import ru.application.habittracker.core.Constants

class ContainerHabitsFragment: Fragment() {

    var callback : HabitListInterface? = null

    companion object {
        fun newInstance() : ContainerHabitsFragment {
            return ContainerHabitsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as HabitListInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_container_habits, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listFragment =
            ListFragment.newInstance()

        val bundle = Bundle()
        bundle.putString("start", "yes")
        listFragment.arguments = bundle

        callback?.openContainerFragment(listFragment)
    }
}