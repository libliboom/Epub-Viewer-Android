package com.github.libliboom.epubviewer.presentation.home.middleware

import com.github.libliboom.epubviewer.domain.home.usecase.HomeUseCase
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action.Business.LoadRank
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action.Business.LoadSection
import com.github.libliboom.epubviewer.presentation.home.HomeStoreFactory.Result
import com.github.libliboom.epubviewer.presentation.home.HomeStoreFactory.Result.Business.LoadedRank
import com.github.libliboom.epubviewer.presentation.home.HomeStoreFactory.Result.Business.LoadedSection
import com.github.libliboom.epubviewer.presentation.home.MiddlewareHandler

class BusinessMiddleware(
  private val onResult: (Result) -> Unit,
  private val usecase: HomeUseCase
) : MiddlewareHandler {
  override fun handleAction(action: Action) {
    when (action) {
      is LoadRank -> handleLoadRank()
      is LoadSection -> handleLoadSection()
    }
  }

  private fun handleLoadRank() {
    val ranks = usecase.getRanks()
    onResult(LoadedRank(ranks))
  }

  private fun handleLoadSection() {
    val sections = usecase.getSections()
    onResult(LoadedSection(sections))
  }
}
