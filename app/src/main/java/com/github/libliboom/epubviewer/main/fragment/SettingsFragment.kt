package com.github.libliboom.epubviewer.main.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.github.libliboom.epubviewer.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}