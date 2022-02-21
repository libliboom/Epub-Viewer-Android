package com.github.libliboom.epubviewer.ui.settings

import android.content.Context
import android.content.Intent
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.databinding.ActivitySettingsBinding
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.State
import com.github.libliboom.epubviewer.ui.viewer.ReaderActivity
import com.github.libliboom.epubviewer.util.resource.StringUtils

interface SettingsViewBinder {
  fun represent(state: State)
}

class SettingsActivity : ReaderActivity<ActivitySettingsBinding>() {

  override fun inflateBinding() = ActivitySettingsBinding.inflate(layoutInflater)

  override fun initView(binding: ActivitySettingsBinding) {
    super.initView(binding)
    updateTitle(StringUtils.getString(R.string.er_toolbar_title_settings))

    supportFragmentManager.beginTransaction()
      .add(R.id.settings_container, SettingsFragment.newInstance())
      .commit()
  }

  companion object {
    fun newIntent(context: Context): Intent {
      return Intent(context, SettingsActivity::class.java)
    }
  }
}
