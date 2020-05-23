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
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_list.*
import ru.application.habittracker.R
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.GetHabitsListInterface
import ru.application.habittracker.data.Data
import ru.application.habittracker.ui.habits.item.AddItemFragment
import ru.application.habittracker.ui.habits.list.tabs.TabsListViewModel

class FilterFragment : Fragment() {

    private lateinit var bottomSheet: LinearLayout
    private lateinit var bottomSheetShow: FrameLayout
    private lateinit var bottomSheetText: TextView
    private lateinit var bottomSheetImg: ImageView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var writeTitle: EditText
    lateinit var  filterLayout: LinearLayout

    lateinit var fab: FloatingActionButton
    var orientationScreenOrActive: String = ""
    var callback : GetHabitsListInterface? = null


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
        callback = activity as GetHabitsListInterface
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

        filterLayout = view.findViewById(R.id.filter_layout)
        writeTitle = view.findViewById(R.id.write_title)

        fab = view.findViewById(R.id.fab)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet) // Вся панель

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetShow.setOnClickListener {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetText.text = "Скрыть фильтр"
                bottomSheetImg.setImageResource(R.drawable.ic_keyboard_arrow_down)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                bottomSheetText.text = "Открыть фильтр"
                bottomSheetImg.setImageResource(R.drawable.ic_keyboard_arrow_up)
            }
        }

        writeTitle.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // Ввод текста нижней панели
                writeTitle.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {
                        callback?.getQueryFilter(writeTitle.text.toString())
                    }
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                })
            }
        }






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
