package ru.application.habittracker

import android.app.Application
import androidx.room.Room.databaseBuilder
import ru.application.habittracker.data.AppDataBase

class App: Application() {
    private lateinit var db: AppDataBase

    override fun onCreate() {
        super.onCreate()

        db = databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "habititem"
        ).build()

    }

    fun getDB(): AppDataBase  {
        return db
    }



}