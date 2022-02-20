package com.github.libliboom.epubviewer.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseActivity

class SettingsActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    // updateTitle(getString(R.string.er_toolbar_title_settings))

    supportFragmentManager.beginTransaction()
      .add(R.id.reader_frame_layout, SettingsFragment.newInstance())
      .commit()
  }

  companion object {
    fun newInstance() = SettingsActivity()

    fun newIntent(context: Context): Intent {
      return Intent(context, SettingsActivity::class.java)
    }
  }
}
