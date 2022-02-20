package com.github.libliboom.epubviewer.presentation.bookshelf

import com.github.libliboom.epubviewer.app.mvi.Middleware
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.Action
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.State
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStoreFactory.Result
import com.github.libliboom.epubviewer.presentation.bookshelf.middleware.UiMiddleware
import java.util.concurrent.atomic.AtomicReference

class BookshelfMiddleware : Middleware<Action, State, Result> {

  private val callbacks = AtomicReference<Middleware.Callbacks<State, Result>>()

  private val uiMiddleware = UiMiddleware(::delegateOnResult)

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
    // no-op
  }
}
