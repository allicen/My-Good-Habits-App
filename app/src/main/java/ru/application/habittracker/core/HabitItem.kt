package ru.application.habittracker.core

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "habits")
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HabitItem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int?,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "priority") val priority: String,
    @ColumnInfo(name = "count") val count: String,
    @ColumnInfo(name = "period") val period: String

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
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