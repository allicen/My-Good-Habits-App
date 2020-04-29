package ru.application.habittracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class AddItemActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_activity)

        if (savedInstanceState == null) {
            val fragment = AddItemFragment.newInstance()

            supportFragmentManager.beginTransaction().add(R.id.add_item_activity, fragment).commit()
        }
    }
}