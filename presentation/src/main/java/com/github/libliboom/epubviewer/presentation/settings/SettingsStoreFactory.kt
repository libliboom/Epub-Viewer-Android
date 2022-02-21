package com.github.libliboom.epubviewer.presentation.settings

import com.github.libliboom.epubviewer.app.mvi.Reducer
import com.github.libliboom.epubviewer.app.mvi.Store
import com.github.libliboom.epubviewer.app.mvi.StoreFactory
import com.github.libliboom.epubviewer.domain.settings.SettingsUsecase
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.Action
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.State
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.StateType.* // ktlint-disable
import com.github.libliboom.epubviewer.presentation.settings.SettingsStoreFactory.Result.* // ktlint-disable

interface MiddlewareHandler {
  fun handleAction(action: Action)
}

class SettingsStoreFactory(
  private val storeFactory: StoreFactory,
  private val usecase: SettingsUsecase
) {

  fun create(): SettingsStore =
    object :
      SettingsStore,
      Store<Action, State> by storeFactory.create(
        name = "Settings",
        initialState = State(),
        middlewareFactory = ::createMiddleware,
        reducer = ReducerImpl()
      ) {}

  private fun createMiddleware() = SettingsMiddleware(usecase)

  sealed class Result {
    sealed class Ui : Result() {
      data class ChangedViewMode(val isVerticalViewMode: Boolean) : Ui()
      data class ChangedAnimationMode(val animationMode: Int) : Ui()
    }
  }

  inner class ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State {
      return when (result) {
        is Ui.ChangedViewMode -> copy(type = CHANGE_VIEW_MODE)
        is Ui.ChangedAnimationMode -> copy(type = CHANGE_ANIMATION_MODE)
      }
    }
  }
}
