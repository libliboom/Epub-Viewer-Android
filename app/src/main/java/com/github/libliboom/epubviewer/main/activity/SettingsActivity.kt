package com.github.libliboom.epubviewer.main.activity

import android.os.Bundle
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.fragment.SettingsFragment

class SettingsActivity : ViewerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_fragment, SettingsFragment.newInstance())
            .commit()
    }

    override fun getTitleToolbar(): String {
        return "Settings"
    }
}
