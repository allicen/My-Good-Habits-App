package ru.application.habittracker.ui.about

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.application.habittracker.R
import ru.application.habittracker.core.GetHabitsListInterface

class AboutAppFragment: Fragment() {

    private lateinit var aboutAppViewModel: AboutAppViewModel

    // Связь с активити
    var callback : GetHabitsListInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as GetHabitsListInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        aboutAppViewModel = ViewModelProviders.of(this)[AboutAppViewModel::class.java] // Модель
        val view = inflater.inflate(R.layout.fragment_about_app, container, false)
        val titleView: TextView = view.findViewById(R.id.about_app_title)
        val descriptionView: TextView = view.findViewById(R.id.about_app_description)
        val versionView: TextView = view.findViewById(R.id.about_app_version)

        aboutAppViewModel.title.observe(this, Observer {
            titleView.text = it
        })
        aboutAppViewModel.description.observe(this, Observer {
            descriptionView.text = it
        })
        aboutAppViewModel.version.observe(this, Observer {
            versionView.text = it
        })

        return view
    }
}