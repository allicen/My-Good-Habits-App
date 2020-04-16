package ru.application.habittracker

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HabitItem (
    val title: String,
    val description: String,
    val type: String,
    val priority: String,
    val count: String,
    val period: String

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(type)
        parcel.writeString(priority)
        parcel.writeString(count)
        parcel.writeString(period)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HabitItem> {
        override fun createFromParcel(parcel: Parcel): HabitItem {
            return HabitItem(parcel)
        }

        override fun newArray(size: Int): Array<HabitItem?> {
            return arrayOfNulls(size)
        }
    }
}