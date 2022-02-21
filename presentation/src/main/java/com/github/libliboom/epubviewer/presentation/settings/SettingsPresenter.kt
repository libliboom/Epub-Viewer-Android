package com.github.libliboom.epubviewer.presentation.settings

import com.github.libliboom.epubviewer.app.mvi.BaseStoreFactory
import com.github.libliboom.epubviewer.domain.settings.SettingsUsecase
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.Action

class SettingsPresenter(usecase: SettingsUsecase) {

  private val store = SettingsStoreFactory(BaseStoreFactory(), usecase).create()
  val state = store.state

  init {
    store.init()
  }

  fun dispatch(action: Action) {
    store.dispatch(action)
  }
}
