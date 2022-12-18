package com.github.libliboom.epubviewer.presentation.home.middleware

import com.github.libliboom.epubviewer.domain.home.usecase.HomeUseCase
import com.github.libliboom.epubviewer.presentation.home.HomeStore.Action
import com.github.libliboom.epubviewer.presentation.home.MiddlewareHandler
import com.github.libliboom.epubviewer.presentation.home.HomeStoreFactory.Result

class UiMiddleware(
  private val onResult: (Result) -> Unit,
  private val usecase: HomeUseCase
) : MiddlewareHandler {
  override fun handleAction(action: Action) {
    // TODO:
  }
}
