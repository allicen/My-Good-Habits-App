package ru.application.habittracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.application.habittracker.core.HabitItem

@Dao
interface FeedDao {
    @Query("SELECT * FROM habititem")
    fun getAll(): LiveData<List<HabitItem>>

//    @Query("SELECT * FROM habititem WHERE id IN (:feedId)")
//    fun getAllById(feedId: String): HabitItem

    @Query("SELECT * FROM habititem WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): HabitItem

    @Insert
    fun insertAll(vararg habitItem: HabitItem)

    @Delete
    fun delete(habitItem: HabitItem)
}
