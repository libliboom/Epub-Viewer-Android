package com.github.libliboom.epubviewer.presentation.settings

import com.github.libliboom.epubviewer.app.mvi.Middleware
import com.github.libliboom.epubviewer.domain.settings.SettingsUsecase
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.Action
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.State
import com.github.libliboom.epubviewer.presentation.settings.SettingsStoreFactory.Result
import com.github.libliboom.epubviewer.presentation.settings.middleware.UiMiddleware
import java.util.concurrent.atomic.AtomicReference

class SettingsMiddleware(usecase: SettingsUsecase) : Middleware<Action, State, Result> {

  private val callbacks = AtomicReference<Middleware.Callbacks<State, Result>>()

  private val uiMiddleware = UiMiddleware(::delegateOnResult, usecase)

  override fun init(callbacks: Middleware.Callbacks<State, Result>) {
    this.callbacks.set(callbacks)
  }

  override fun handleAction(action: Action) {
    when (action) {
      is Action.Ui -> uiMiddleware.handleAction(action)
    }
  }

  private fun delegateOnResult(result: Result) {
    callbacks.get().onResult(result)
  }

  override fun dispose() {
    TODO("Not yet implemented")
  }
}
