package com.github.libliboom.epubviewer.domain.settings

class SettingsUsecase(private val repository: ISettingsRepository) {
  fun getViewMode() = repository.getViewMode()
  fun setViewMode(state: Boolean) = repository.setViewMode(state)
  fun getAnimationMode() { repository.getAnimationMode() }
  fun setAnimationMode(mode: Int) = repository.setAnimationMode(mode)
}
