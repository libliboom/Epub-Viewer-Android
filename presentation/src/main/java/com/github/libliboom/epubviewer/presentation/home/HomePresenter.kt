package com.github.libliboom.epubviewer.presentation.home

import com.github.libliboom.epubviewer.app.mvi.BaseStoreFactory
import com.github.libliboom.epubviewer.domain.home.usecase.HomeUseCase
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action

class HomePresenter(useCase: HomeUseCase) {

  private val store = HomeStoreFactory(BaseStoreFactory(), useCase).create()
  val state = store.state

  init {
    store.init()
  }

  fun dispatch(action: Action) {
    store.dispatch(action)
  }
}
