package com.github.libliboom.epubviewer.presentation.home

import com.github.libliboom.epubviewer.app.mvi.Middleware
import com.github.libliboom.epubviewer.domain.home.usecase.HomeUseCase
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action.Business
import com.github.libliboom.epubviewer.presentation.home.HomeStore.State
import com.github.libliboom.epubviewer.presentation.home.HomeStoreFactory.Result
import com.github.libliboom.epubviewer.presentation.home.middleware.BusinessMiddleware
import com.github.libliboom.epubviewer.presentation.home.middleware.UiMiddleware
import java.util.concurrent.atomic.AtomicReference

class HomeMiddleware(useCase: HomeUseCase) : Middleware<Action, State, Result> {

  private val callbacks = AtomicReference<Middleware.Callbacks<State, Result>>()

  private val uiMiddleware = UiMiddleware(::delegateOnResult, useCase)
  private val businessMiddleware = BusinessMiddleware(::delegateOnResult, useCase)

  override fun init(callbacks: Middleware.Callbacks<State, Result>) {
    this.callbacks.set(callbacks)
  }

  override fun handleAction(action: Action) {
    when (action) {
      is Business.LoadRank,
      is Business.LoadSection
      -> businessMiddleware.handleAction(action)
    }
  }

  private fun delegateOnResult(result: Result) {
    callbacks.get().onResult(result)
  }

  override fun dispose() {
    TODO("Not yet implemented")
  }
}
