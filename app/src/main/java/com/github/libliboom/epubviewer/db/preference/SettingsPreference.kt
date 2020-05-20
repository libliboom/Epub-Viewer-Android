@file:Suppress("DEPRECATION")

package com.github.libliboom.epubviewer.db.preference

import android.content.Context
import android.preference.PreferenceManager

object SettingsPreference {

    private const val PREF_VIEW_MODE = "view_mode"

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
}