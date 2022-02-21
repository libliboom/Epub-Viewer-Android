package com.github.libliboom.epubviewer.datasource.settings

import com.github.libliboom.epubviewer.repository.settings.ISettingsDatasource

class SettingsLocalDatasource : ISettingsDatasource {
  override fun getViewMode() = SettingsPreference.getViewMode()
  override fun setViewMode(state: Boolean) = SettingsPreference.setViewMode(state)
  override fun getAnimationMode() { SettingsPreference.getAnimationMode() }
  override fun setAnimationMode(mode: Int) = SettingsPreference.setAnimationMode(mode)
}
