package com.github.libliboom.epubviewer.presentation.contents

import com.github.libliboom.epubviewer.app.mvi.Middleware
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.Action
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.State
import com.github.libliboom.epubviewer.presentation.contents.ContentsStoreFactory.Result
import com.github.libliboom.epubviewer.presentation.contents.middleware.NavMiddleware
import com.github.libliboom.epubviewer.presentation.contents.middleware.UiMiddleware
import java.util.concurrent.atomic.AtomicReference

class ContentsMiddleware : Middleware<Action, State, Result> {

  private val callbacks = AtomicReference<Middleware.Callbacks<State, Result>>()

  private val uiMiddleware = UiMiddleware(::delegateOnResult)
  private val navMiddleware = NavMiddleware(::delegateOnResult)

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
