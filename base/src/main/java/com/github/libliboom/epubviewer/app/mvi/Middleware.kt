package com.github.libliboom.epubviewer.app.mvi

import androidx.annotation.MainThread

interface Middleware<Action, State, out Result : Any> {

  @MainThread
  fun init(callbacks: Callbacks<State, Result>)

  @MainThread
  fun handleAction(action: Action)

  @MainThread
  fun dispose()

  interface Callbacks<out State, in Result> {
    @MainThread
    fun onResult(result: Result)
  }
}
