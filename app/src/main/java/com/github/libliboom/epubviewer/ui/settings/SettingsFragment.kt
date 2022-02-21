package com.github.libliboom.epubviewer.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.domain.settings.SettingsUsecase
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.Action
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.StateType.CHANGE_ANIMATION_MODE
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.StateType.CHANGE_VIEW_MODE
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener, SettingsViewBinder {

  @Inject
  lateinit var usecase: SettingsUsecase

  private val viewModel: SettingsViewModel by viewModels()

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.bind(usecase)
    viewModel.bind(this)
  }

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

    when (preference.key) {
      KEY_VIEW_MODE -> viewModel.dispatch(Action.Ui.ChangeViewMode(newValue as Boolean))
      KEY_ANIMATION_MODE -> viewModel.dispatch(Action.Ui.ChangeAnimationMode((newValue.toString().toInt())))
      else -> Unit
    }

    return true
  }

  override fun represent(state: SettingsStore.State) {
    when (state.type) {
      CHANGE_VIEW_MODE -> representChangeViewMode()
      CHANGE_ANIMATION_MODE -> representChangeAnimationMode()
      else -> Unit
    }
  }

  private fun representChangeViewMode() {
    Intent().run {
      putExtra(EXTRA_SETTINGS_VIEW_MODE, true)
      sendResult(this)
    }
  }

  private fun representChangeAnimationMode() {
    val intent = Intent()
    intent.putExtra(EXTRA_SETTINGS_ANIMATION_MODE, true)
    sendResult(intent)
  }

  private fun sendResult(intent: Intent) {
    requireActivity().run {
      setResult(Activity.RESULT_OK, intent)
      finish()
    }
  }

  companion object {
    const val KEY_VIEW_MODE = "key_view_mode"
    const val KEY_ANIMATION_MODE = "key_animation_mode"
    const val EXTRA_SETTINGS_VIEW_MODE = "com.github.libliboom.epubviewer.main.fragment.view_mode"
    const val EXTRA_SETTINGS_ANIMATION_MODE = "com.github.libliboom.epubviewer.main.fragment.animation_mode"

    fun newInstance() = SettingsFragment()
  }
}
