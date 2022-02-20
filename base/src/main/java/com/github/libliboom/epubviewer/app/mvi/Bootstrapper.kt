package com.github.libliboom.epubviewer.app.mvi

import androidx.annotation.MainThread

typealias ActionConsumer<Action> = (Action) -> Unit

interface Bootstrapper<Action> {

  @MainThread
  fun init(actionConsumer: ActionConsumer<Action>)

  @MainThread
  operator fun invoke()

  @MainThread
  fun dispose()
}
