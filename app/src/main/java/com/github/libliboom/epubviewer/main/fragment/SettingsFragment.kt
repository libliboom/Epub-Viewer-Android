package com.github.libliboom.epubviewer.main.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.db.preference.SettingsPreference

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        preferenceScreen.apply {
            findPreference<SwitchPreferenceCompat>(KEY_VIEW_MODE)?.onPreferenceChangeListener = this@SettingsFragment
            findPreference<ListPreference>(KEY_ANIMATION_MODE)?.onPreferenceChangeListener = this@SettingsFragment
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        preference ?: return true
        newValue ?: return true

        preference.run {
            when (key) {
                KEY_VIEW_MODE -> {
                    SettingsPreference.setViewMode(requireContext(), newValue as Boolean)
                    val intent = Intent()
                    intent.putExtra(EXTRA_SETTINGS_VIEW_MODE, true)
                    sendResult(intent)
                }
                KEY_ANIMATION_MODE -> {
                    val n: String = newValue as String
                    SettingsPreference.setAnimationMode(requireContext(), n.toInt())
                    val intent = Intent()
                    intent.putExtra(EXTRA_SETTINGS_ANIMATION_MODE, true)
                    sendResult(intent)
                }
                else -> Log.i(TAG, "onPreferenceChange: else")
            }
        }

        return true
    }

    private fun sendResult(intent: Intent) {
        requireActivity().run {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val TAG = "SettingsFragment"
        const val KEY_VIEW_MODE = "key_view_mode"
        const val KEY_ANIMATION_MODE = "key_animation_mode"
        const val EXTRA_SETTINGS_VIEW_MODE = "com.github.libliboom.epubviewer.main.fragment.view_mode"
        const val EXTRA_SETTINGS_ANIMATION_MODE = "com.github.libliboom.epubviewer.main.fragment.animation_mode"

        fun newInstance() = SettingsFragment()
    }
}