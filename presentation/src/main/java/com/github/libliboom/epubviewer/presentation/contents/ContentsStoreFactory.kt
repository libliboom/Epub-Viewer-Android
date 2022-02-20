package com.github.libliboom.epubviewer.presentation.contents

import com.github.libliboom.epubviewer.app.mvi.Reducer
import com.github.libliboom.epubviewer.app.mvi.Store
import com.github.libliboom.epubviewer.app.mvi.StoreFactory
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.Action
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.State
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.StateType.CLICK_CONTENTS

interface MiddlewareHandler {
  fun handleAction(action: Action)
}

class ContentsStoreFactory(private val storeFactory: StoreFactory) {

  fun create(): ContentsStore =
    object :
      ContentsStore,
      Store<Action, State> by storeFactory.create(
        name = "Contents",
        initialState = State(),
        middlewareFactory = ::createMiddleware,
        reducer = ReducerImpl()
      ) {}

  private fun createMiddleware() = ContentsMiddleware()

  sealed class Result {
    sealed class Ui : Result() {
      data class ClickedContents(val text: String) : Ui()
    }
  }

  inner class ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State {
      return when (result) {
        is Result.Ui.ClickedContents -> copy(type = CLICK_CONTENTS, contentName = result.text)
      }
    }
  }
}
