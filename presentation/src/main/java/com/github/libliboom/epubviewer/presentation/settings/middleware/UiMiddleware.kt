package com.github.libliboom.epubviewer.presentation.settings.middleware

import com.github.libliboom.epubviewer.domain.settings.SettingsUsecase
import com.github.libliboom.epubviewer.presentation.settings.MiddlewareHandler
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.Action
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.Action.Ui.ChangeAnimationMode
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.Action.Ui.ChangeViewMode
import com.github.libliboom.epubviewer.presentation.settings.SettingsStoreFactory.Result
import com.github.libliboom.epubviewer.presentation.settings.SettingsStoreFactory.Result.Ui.ChangedAnimationMode
import com.github.libliboom.epubviewer.presentation.settings.SettingsStoreFactory.Result.Ui.ChangedViewMode

class UiMiddleware(
  private val onResult: (Result) -> Unit,
  private val usecase: SettingsUsecase
) : MiddlewareHandler {
  override fun handleAction(action: Action) {
    when (action) {
      is ChangeViewMode -> handleChangeViewMode(action)
      is ChangeAnimationMode -> handleChangeAnimationMode(action)
    }
  }

  private fun handleChangeViewMode(action: ChangeViewMode) {
    usecase.setViewMode(action.isVerticalViewMode)
    onResult(ChangedViewMode(action.isVerticalViewMode))
  }

  private fun handleChangeAnimationMode(action: ChangeAnimationMode) {
    usecase.setAnimationMode(action.animationMode)
    onResult(ChangedAnimationMode(action.animationMode))
  }
}
