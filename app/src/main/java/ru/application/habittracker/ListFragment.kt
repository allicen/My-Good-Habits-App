package ru.application.habittracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.io.Serializable
import java.util.ArrayList

class ListFragment: Fragment(), Serializable {
    var habitList: ArrayList<HabitItem> = ArrayList()
    var adapter: RecAdapter = RecAdapter(habitList)
    var position: Int = Constants.ITEM_POSITION_DEFAULT

    companion object {
        private const val POSITION = "position"
        private const val HABBIT_LIST = "habbit_list"
        private const val ADAPTER = "adapter"

        fun newInstance(habitList: ArrayList<HabitItem>, adapter: RecAdapter, position: Int) : ListFragment {
            val fragment = ListFragment()
            val bundle = Bundle()
            bundle.putInt(POSITION, position)
            bundle.putSerializable(HABBIT_LIST, habitList)
            bundle.putSerializable(ADAPTER, adapter)
            fragment.arguments = bundle
            return fragment
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            habitList = it.getParcelableArrayList(HABBIT_LIST) ?: ArrayList()
            adapter = (it.getSerializable(ADAPTER) ?: RecAdapter(habitList)) as RecAdapter
            position = it.getInt(POSITION) ?: Constants.ITEM_POSITION_DEFAULT
        }
    }
}