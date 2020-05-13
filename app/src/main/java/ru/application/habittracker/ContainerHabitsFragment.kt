package ru.application.habittracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

class ContainerHabitsFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_container_habits, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listFragment = ListFragment.newInstance()

        if (activity?.supportFragmentManager?.findFragmentByTag("list") != null) {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container_habits_fragment, listFragment, "list")
                ?.addToBackStack("main")?.commitAllowingStateLoss()
        } else {
            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.container_habits_fragment, listFragment, "list")
                ?.addToBackStack("main")?.commitAllowingStateLoss()
        }
    }
}