package com.github.libliboom.epubviewer.presentation.contents.middleware

import com.github.libliboom.epubviewer.presentation.contents.ContentsStore
import com.github.libliboom.epubviewer.presentation.contents.ContentsStoreFactory
import com.github.libliboom.epubviewer.presentation.contents.MiddlewareHandler

class NavMiddleware(private val onResult: (ContentsStoreFactory.Result) -> Unit) : MiddlewareHandler {
  override fun handleAction(action: ContentsStore.Action) {
    when (action) {
      else -> {}
    }
  }
}
