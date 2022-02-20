@file:Suppress("DEPRECATION")

package com.github.libliboom.epubviewer.db.preference

import android.content.Context
import android.preference.PreferenceManager

object SettingsPreference {

  private const val PREF_VIEW_MODE = "view_mode"
  private const val PREF_ANIMATION_MODE = "animation_mode"

  fun getViewMode(context: Context?): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(context)
      .getBoolean(PREF_VIEW_MODE, false)
  }

  fun setViewMode(context: Context?, state: Boolean) {
    PreferenceManager.getDefaultSharedPreferences(context)
      .edit()
      .putBoolean(PREF_VIEW_MODE, state)
      .apply()
  }

  fun getAnimationMode(context: Context?): Int {
    return PreferenceManager.getDefaultSharedPreferences(context)
      .getInt(PREF_ANIMATION_MODE, 0)
  }

  fun setAnimationMode(context: Context?, mode: Int) {
    PreferenceManager.getDefaultSharedPreferences(context)
      .edit()
      .putInt(PREF_ANIMATION_MODE, mode)
      .apply()
  }
}
