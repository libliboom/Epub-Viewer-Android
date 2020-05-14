package com.github.libliboom.epubviewer.main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.fragment.SettingsFragment
import com.github.libliboom.epubviewer.reader.activity.ReaderActivity

class SettingsActivity : ReaderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        updateTitle(getString(R.string.er_toolbar_title_settings))

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_fragment, SettingsFragment.newInstance())
            .commit()
    }

    companion object {
        fun newInstance() = SettingsActivity()

        fun newIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}
