package ru.application.habittracker.ui.habits.filter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.HabitListInterface
import ru.application.habittracker.R
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.data.FeedDao
import ru.application.habittracker.ui.habits.item.AddItemFragment
import ru.application.habittracker.ui.habits.list.ListFragment
import ru.application.habittracker.ui.habits.list.tabs.ListViewModel
import ru.application.habittracker.ui.habits.list.tabs.ListViewModelFactory

class FilterFragment : Fragment() {

    private lateinit var bottomSheet: LinearLayout
    private lateinit var bottomSheetShow: FrameLayout
    private lateinit var bottomSheetText: TextView
    private lateinit var bottomSheetImg: ImageView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var writeTitle: EditText
    lateinit var  filterLayout: LinearLayout
    lateinit var bottomPanelWithForm: LinearLayout

    lateinit var fab: FloatingActionButton
    var orientationScreenOrActive: String = ""
    var callback : HabitListInterface? = null

    lateinit var dao: FeedDao

    companion object{
        fun newInstance(): FilterFragment {
            return FilterFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("########onCreate filter")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as HabitListInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_filter_constrain, container, false)

        bottomSheet = view.findViewById(R.id.bottom_sheet_layout)
        bottomSheetShow = view.findViewById(R.id.bottom_sheet)
        bottomSheetText = view.findViewById(R.id.show_bottom_panel) // Текст
        bottomSheetImg = view.findViewById(R.id.show_bottom_panel_arrow) // Иконка
        bottomPanelWithForm = view.findViewById(R.id.bottom_panel) // Форма

        filterLayout = view.findViewById(R.id.filter_layout)
       // writeTitle = view.findViewById(R.id.write_title)

        fab = view.findViewById(R.id.fab)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet) // Вся панель

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Показать/скрыть панель в зависимости от наличия списка
//        val feed: LiveData<List<HabitItem>> = callback?.getContextFromApp()?.getAll()!!
//        feed.observe(viewLifecycleOwner, Observer { feeds ->
//            val habitList = feeds as ArrayList<HabitItem>
//
//            if (habitList.size == 0) {
//                bottomPanelWithForm.visibility = View.INVISIBLE
//            } else {
//                bottomPanelWithForm.visibility = View.VISIBLE
//            }
//        })



//
//
//        bottomSheetShow.setOnClickListener {
//            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
//                bottomSheetText.text = "Скрыть фильтр"
//                bottomSheetImg.setImageResource(R.drawable.ic_keyboard_arrow_down)
//            } else {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
//                bottomSheetText.text = "Открыть фильтр"
//                bottomSheetImg.setImageResource(R.drawable.ic_keyboard_arrow_up)
//            }
//        }


        fab.setOnClickListener {
            Log.e("tag", "Открыто окно создания привычки")

            if (callback?.isLand() == true) {
                orientationScreenOrActive = "land"
            } else {
                orientationScreenOrActive = "add"
            }

            val bundle = Bundle()
            bundle.putString("orientationScreenOrActive", orientationScreenOrActive)
            bundle.putInt("positions",
                Constants.ITEM_POSITION_DEFAULT
            )

            val addItemFragment = AddItemFragment.newInstance()
            addItemFragment.arguments = bundle

            callback?.openAddItemFragment(addItemFragment)
        }
    }
}
