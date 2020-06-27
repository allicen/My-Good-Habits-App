package ru.application.habittracker.core

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "habits")
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HabitItem(
    @SerializedName("uid") @NonNull @PrimaryKey() @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "count") val count: Int,
    @SerializedName("frequency") @ColumnInfo(name = "period") val period: String

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeInt(type)
        parcel.writeInt(priority)
        parcel.writeInt(count)
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