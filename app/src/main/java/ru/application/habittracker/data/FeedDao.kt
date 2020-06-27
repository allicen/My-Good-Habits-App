package ru.application.habittracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.application.habittracker.core.HabitItem

@Dao
interface FeedDao {
    @Query("SELECT * FROM habits")
    fun getAll(): LiveData<List<HabitItem>>

    @Query("SELECT * FROM habits WHERE id IN (:feedId)")
    fun getAllById(feedId: String): LiveData<HabitItem>

    @Query("SELECT * FROM habits WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): LiveData<HabitItem>

    @Query("SELECT * FROM habits WHERE title LIKE :title LIMIT 1")
    fun findById(title: String): HabitItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: HabitItem) : Long

    @Insert
    fun insertAll(vararg habitItem: HabitItem)

    @Update
    fun update(entity: HabitItem)

    @Delete
    fun delete(habitItem: HabitItem)

    @Query("DELETE FROM habits WHERE id = :feedId")
    fun deleteById(feedId: String)
}
