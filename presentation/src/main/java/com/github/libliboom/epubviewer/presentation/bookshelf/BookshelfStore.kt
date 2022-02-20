package com.github.libliboom.epubviewer.presentation.bookshelf

import com.github.libliboom.epubviewer.app.mvi.Store
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.Action
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.State

interface BookshelfStore : Store<Action, State> {

  data class State(
    val type: StateType = StateType.INIT,
    val clickedIndex: Int = 0
  )

  enum class StateType {
    INIT, UPDATED_LIST, BOOK_CLICKED
  }

  sealed class Action {
    sealed class Ui : Action() {
      object UpdateList : Ui()
      class ClickBook(val index: Int) : Ui()
    }
  }
}
