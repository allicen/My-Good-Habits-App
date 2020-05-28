package ru.application.habittracker.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutAppViewModel : ViewModel() {
    private val _title = MutableLiveData<String>().apply {
        value = "О приложении"
    }

    private val _description = MutableLiveData<String>().apply {
        value = "Приложение «Трекер привычек» позволит контролировать ваши привычки: вырабатывать хорошие и отказываться от плохих."
    }

    private val _version = MutableLiveData<String>().apply {
        value = "Версия: 1.0.0"
    }

    val title: LiveData<String> = _title
    val description: LiveData<String> = _description
    val version: LiveData<String> = _version
}