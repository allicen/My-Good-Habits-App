package ru.application.habittracker.core

import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

interface GetHabitsListInterface : ListInterface {
    fun updateHabitListFromFragmentData(data: HabitItem, position: Int, delete: Boolean = false): ArrayList<HabitItem>
}