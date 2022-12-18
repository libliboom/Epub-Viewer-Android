package com.github.libliboom.epubviewer.presentation.home

import com.github.libliboom.epubviewer.app.mvi.Store
import com.github.libliboom.epubviewer.domain.home.Section
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action
import com.github.libliboom.epubviewer.presentation.home.HomeStore.State

interface HomeStore : Store<Action, State> {

  data class State(
    val type: StateType = StateType.INIT,
    val sections: List<Section> = listOf()
  )

  enum class StateType {
    INIT, LOAD_SECTIONS
  }

  sealed class Action {
    sealed class Ui : Action()
    sealed class Business : Action() {
      object LoadRank : Business()
      object LoadSection : Business()
    }
  }
}
