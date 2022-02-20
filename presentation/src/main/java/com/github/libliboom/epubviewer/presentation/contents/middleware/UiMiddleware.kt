package com.github.libliboom.epubviewer.presentation.contents.middleware

import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.Action
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.Action.Ui
import com.github.libliboom.epubviewer.presentation.contents.ContentsStoreFactory
import com.github.libliboom.epubviewer.presentation.contents.ContentsStoreFactory.Result.Ui.ClickedContents
import com.github.libliboom.epubviewer.presentation.contents.MiddlewareHandler

class UiMiddleware(private val onResult: (ContentsStoreFactory.Result) -> Unit) : MiddlewareHandler {
  override fun handleAction(action: Action) {
    when (action) {
      is Ui.ClickContents -> onResult(ClickedContents(action.text))
    }
  }
}
