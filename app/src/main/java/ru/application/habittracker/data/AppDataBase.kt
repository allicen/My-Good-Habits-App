package ru.application.habittracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.application.habittracker.core.HabitItem

@Database(entities = [HabitItem::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    public abstract fun feedDao(): FeedDao
}