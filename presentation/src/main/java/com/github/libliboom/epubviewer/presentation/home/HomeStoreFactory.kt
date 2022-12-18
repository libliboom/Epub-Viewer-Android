package com.github.libliboom.epubviewer.presentation.home

import com.github.libliboom.epubviewer.app.mvi.Reducer
import com.github.libliboom.epubviewer.app.mvi.Store
import com.github.libliboom.epubviewer.app.mvi.StoreFactory
import com.github.libliboom.epubviewer.domain.home.Section
import com.github.libliboom.epubviewer.domain.home.usecase.HomeUseCase
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action
import com.github.libliboom.epubviewer.presentation.home.HomeStore.State
import com.github.libliboom.epubviewer.presentation.home.HomeStore.StateType.LOAD_SECTIONS
import com.github.libliboom.epubviewer.presentation.home.HomeStoreFactory.Result.Business

interface MiddlewareHandler {
  fun handleAction(action: HomeStore.Action)
}

class HomeStoreFactory(
  private val storeFactory: StoreFactory,
  private val usecase: HomeUseCase
) {
  fun create(): HomeStore =
    object :
      HomeStore,
      Store<Action, State> by storeFactory.create(
        name = "Home",
        initialState = State(),
        middlewareFactory = ::createMiddleware,
        reducer = ReducerImpl()
      ) {}

  private fun createMiddleware() = HomeMiddleware(usecase)

  sealed class Result {
    sealed class Ui : Result()
    sealed class Business : Result() {
      data class LoadedRank(val ranks: List<Section>) : Business()
      data class LoadedSection(val sections: List<Section>) : Business()
    }
  }

  inner class ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State {
      return when (result) {
        is Business.LoadedRank -> reduceLoadedRank(result)
        is Business.LoadedSection -> reduceLoadedSection(result)
      }
    }

    private fun State.reduceLoadedRank(result: Business.LoadedRank) =
      copy(
        type = LOAD_SECTIONS,
        sections = result.ranks + this.sections
      )

    private fun State.reduceLoadedSection(result: Business.LoadedSection) =
      copy(
        type = LOAD_SECTIONS,
        sections = this.sections + result.sections
      )
  }
}
