package com.github.libliboom.epubviewer.presentation.contents

import com.github.libliboom.epubviewer.app.mvi.Store
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.Action
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.State

interface ContentsStore : Store<Action, State> {

  data class State(
    val type: StateType = StateType.INIT,
    val contentName: String = ""
  )

  enum class StateType {
    INIT, CLICK_CONTENTS
  }

  sealed class Action {
    sealed class Ui : Action() {
      data class ClickContents(val text: String) : Ui()
    }
  }
}
