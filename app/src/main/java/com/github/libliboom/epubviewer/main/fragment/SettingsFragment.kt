package com.github.libliboom.epubviewer.main.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.db.preference.SettingsPreference

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val viewMode = preferenceScreen.findPreference<SwitchPreferenceCompat>(KEY_VIEW_MODE)
        viewMode?.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        preference ?: return true
        newValue ?: return true

        preference.run {
            when (key) {
                KEY_VIEW_MODE -> {
                    SettingsPreference.setViewMode(requireContext(), newValue as Boolean)
                    sendResult()
                }
                else -> Log.i(TAG, "onPreferenceChange: else")
            }
        }

        return true
    }

    private fun sendResult() {
        requireActivity().run {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }

    companion object {
        const val TAG = "SettingsFragment"
        const val KEY_VIEW_MODE = "key_view_mode"

        fun newInstance() = SettingsFragment()
    }
}