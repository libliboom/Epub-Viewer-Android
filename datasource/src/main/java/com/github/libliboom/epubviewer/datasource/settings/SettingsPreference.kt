@file:Suppress("DEPRECATION")

package com.github.libliboom.epubviewer.datasource.settings

import android.app.Application
import android.preference.PreferenceManager
import com.github.libliboom.epubviewer.app.ui.ApplicationInitializer

object SettingsPreference : ApplicationInitializer {

  private const val PREF_VIEW_MODE = "view_mode"
  private const val PREF_ANIMATION_MODE = "animation_mode"

  private lateinit var application: Application
  private val context get() = application.applicationContext

  override fun init(application: Application) {
    SettingsPreference.application = application
  }

  fun getViewMode(): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(context)
      .getBoolean(PREF_VIEW_MODE, false)
  }

  fun setViewMode(state: Boolean) {
    PreferenceManager.getDefaultSharedPreferences(context)
      .edit()
      .putBoolean(PREF_VIEW_MODE, state)
      .apply()
  }

  fun getAnimationMode(): Int {
    return PreferenceManager.getDefaultSharedPreferences(context)
      .getInt(PREF_ANIMATION_MODE, 0)
  }

  fun setAnimationMode(mode: Int) {
    PreferenceManager.getDefaultSharedPreferences(context)
      .edit()
      .putInt(PREF_ANIMATION_MODE, mode)
      .apply()
  }
}
