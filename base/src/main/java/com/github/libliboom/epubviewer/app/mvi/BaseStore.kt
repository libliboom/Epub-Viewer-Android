package com.github.libliboom.epubviewer.app.mvi

import android.util.Log
import androidx.annotation.MainThread
import com.github.libliboom.epubviewer.app.mvi.Store.Companion.TAG
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BaseStore<Action, State, in Result : Any> @MainThread constructor(
  initialState: State,
  private val bootstrapper: Bootstrapper<Action>?,
  private val middleware: Middleware<Action, State, Result>,
  private val reducer: Reducer<State, Result>
) : Store<Action, State> {

  override val actionChannel = Channel<Action>()
  override val state = MutableStateFlow(initialState)

  @MainThread
  override fun init() {
    fun initBootstrapper() {
      bootstrapper?.run {
        init { action -> middleware.handleAction(action) }
        invoke()
      }
    }
    fun initMiddleware() {
      middleware.init(
        object : Middleware.Callbacks<State, Result> {
          override fun onResult(result: Result) {
            applyState(result)
          }
        }
      )
    }

    initBootstrapper()
    initMiddleware()
    initChannel()
  }

  @MainThread
  private fun initChannel() {
    storeScope.launch {
      actionChannel.receiveAsFlow()
        .map { middleware.handleAction(it) }
        .catch { Log.e(TAG, "Error of Action: $it") }
        .collect()
    }
  }

  @MainThread
  override fun dispose() {
    actionChannel.cancel()
  }

  @MainThread
  override fun dispatch(action: Action) {
    storeScope.launch {
      actionChannel.send(action)
    }
  }

  @MainThread
  private fun applyState(result: Result) {
    state.update { reducer.run { it.reduce(result) } }
  }
}
