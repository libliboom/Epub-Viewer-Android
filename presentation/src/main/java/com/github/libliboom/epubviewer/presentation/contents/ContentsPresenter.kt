package com.github.libliboom.epubviewer.presentation.contents

import com.github.libliboom.epubviewer.app.mvi.BaseStoreFactory
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.Action

class ContentsPresenter {

  private val store = ContentsStoreFactory(BaseStoreFactory()).create()
  val state = store.state

  init {
    store.init()
  }

  fun dispatch(action: Action) {
    store.dispatch(action)
  }
}
