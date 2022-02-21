package com.github.libliboom.epubviewer.repository.settings

import com.github.libliboom.epubviewer.domain.settings.ISettingsRepository

class SettingsRepository(val datasource: ISettingsDatasource) : ISettingsRepository {
  override fun getViewMode() = datasource.getViewMode()
  override fun setViewMode(state: Boolean) = datasource.setViewMode(state)
  override fun getAnimationMode() = datasource.getAnimationMode()
  override fun setAnimationMode(mode: Int) = datasource.setAnimationMode(mode)
}
