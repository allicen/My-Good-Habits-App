package ru.application.habittracker.ui.habits.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.application.habittracker.R
import ru.application.habittracker.data.Data

class FilterFragment : Fragment() {

    private lateinit var bottomSheet: LinearLayout
    private lateinit var bottomSheetShow: FrameLayout
    private lateinit var bottomSheetText: TextView
    private lateinit var bottomSheetImg: ImageView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var writeTitle: EditText


    companion object{
        fun newInstance(): FilterFragment {
            return FilterFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        writeTitle = view.findViewById(R.id.write_title)

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

        // Ввод текста нижней панели
        writeTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                habitTitleFilter(writeTitle.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })


    }

    @SuppressLint("DefaultLocale")
    fun habitTitleFilter(query: String) {
        val habits = Data.habitList.filter { it.title.indexOf(query.toLowerCase()) != -1 }
    }

}
