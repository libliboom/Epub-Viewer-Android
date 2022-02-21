package com.github.libliboom.epubviewer.ui.settings

import android.content.Context
import android.content.Intent
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.databinding.ActivitySettingsBinding
import com.github.libliboom.epubviewer.ui.viewer.ReaderActivity

class SettingsActivity : ReaderActivity<ActivitySettingsBinding>() {

  override fun inflateBinding() = ActivitySettingsBinding.inflate(layoutInflater)

  override fun initView(binding: ActivitySettingsBinding) {
    super.initView(binding)
    updateTitle(getString(R.string.er_toolbar_title_settings))

    supportFragmentManager.beginTransaction()
      .add(R.id.settings_container, SettingsFragment.newInstance())
      .commit()
  }

  companion object {
    fun newInstance() = SettingsActivity()

    fun newIntent(context: Context): Intent {
      return Intent(context, SettingsActivity::class.java)
    }
  }
}
