package com.github.libliboom.epubviewer.presentation.settings

import com.github.libliboom.epubviewer.app.mvi.Store
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.Action
import com.github.libliboom.epubviewer.presentation.settings.SettingsStore.State

interface SettingsStore : Store<Action, State> {

  data class State(
    val type: StateType = StateType.INIT
  )

  enum class StateType {
    INIT, CHANGE_VIEW_MODE, CHANGE_ANIMATION_MODE
  }

  sealed class Action {
    sealed class Ui : Action() {
      data class ChangeViewMode(val isVerticalViewMode: Boolean) : Ui()
      data class ChangeAnimationMode(val animationMode: Int) : Ui()
    }
  }
}
