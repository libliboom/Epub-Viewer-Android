package com.github.libliboom.epubviewer.app.mvi

import androidx.annotation.MainThread

/* ktlint-disable expecting function name or receiver type) */
fun interface Reducer<State, in Result> {
  @MainThread
  fun State.reduce(result: Result): State
}
/* ktlint-enable expecting function name or receiver type) */
