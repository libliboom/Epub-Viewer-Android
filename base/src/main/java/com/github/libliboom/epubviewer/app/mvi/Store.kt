package com.github.libliboom.epubviewer.app.mvi

import android.util.Log
import androidx.annotation.MainThread
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow

interface Store<Action, State> {

  val actionChannel: Channel<Action>
  val state: MutableStateFlow<State>
  val isDisposed get() = actionChannel.isClosedForSend

  val storeJob get() = Job()
  val storeExceptionHandler get() = CoroutineExceptionHandler { _, throwable -> log(throwable) }
  val storeScope get() = CoroutineScope(storeJob + storeExceptionHandler)

  @MainThread
  fun init()

  @MainThread
  fun dispose()

  @MainThread
  fun dispatch(action: Action)

  private fun log(throwable: Throwable) {
    Log.e(TAG, "Error of Store: $throwable")
  }

  companion object {
    val TAG: String = Store::class.java.simpleName
  }
}
