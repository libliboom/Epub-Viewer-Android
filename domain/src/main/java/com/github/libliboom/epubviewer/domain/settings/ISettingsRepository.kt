package com.github.libliboom.epubviewer.domain.settings

interface ISettingsRepository {
  fun getViewMode(): Boolean
  fun setViewMode(state: Boolean)
  fun getAnimationMode()
  fun setAnimationMode(mode: Int)
}
