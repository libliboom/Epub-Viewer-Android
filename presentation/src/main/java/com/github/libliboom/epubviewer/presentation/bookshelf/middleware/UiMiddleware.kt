package com.github.libliboom.epubviewer.presentation.bookshelf.middleware

import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.Action
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.Action.Ui.ClickBook
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.Action.Ui.UpdateList
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStoreFactory
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStoreFactory.Result.Ui.ClickedBook
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStoreFactory.Result.Ui.UpdatedList
import com.github.libliboom.epubviewer.presentation.bookshelf.MiddlewareHandler

class UiMiddleware(private val onResult: (BookshelfStoreFactory.Result) -> Unit) : MiddlewareHandler {
  override fun handleAction(action: Action) {
    when (action) {
      is UpdateList -> onResult(UpdatedList)
      is ClickBook -> onResult(ClickedBook(action.index))
    }
  }
}
