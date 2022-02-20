package com.github.libliboom.epubviewer.presentation.bookshelf

import com.github.libliboom.epubviewer.app.mvi.Reducer
import com.github.libliboom.epubviewer.app.mvi.Store
import com.github.libliboom.epubviewer.app.mvi.StoreFactory
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.Action
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.State
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.StateType.BOOK_CLICKED
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.StateType.UPDATED_LIST
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStoreFactory.Result.Ui

interface MiddlewareHandler {
  fun handleAction(action: Action)
}

class BookshelfStoreFactory(private val storeFactory: StoreFactory) {

  fun create(): BookshelfStore =
    object :
      BookshelfStore,
      Store<Action, State> by storeFactory.create(
        name = "Bookshelf",
        initialState = State(),
        middlewareFactory = ::createMiddleware,
        reducer = ReducerImpl()
      ) {}

  private fun createMiddleware() = BookshelfMiddleware()

  sealed class Result {
    sealed class Ui : Result() {
      object UpdatedList : Ui()
      class ClickedBook(val index: Int) : Ui()
    }
  }

  inner class ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State {
      return when (result) {
        is Ui.UpdatedList -> copy(type = UPDATED_LIST)
        is Ui.ClickedBook -> copy(type = BOOK_CLICKED, clickedIndex = result.index)
      }
    }
  }
}
