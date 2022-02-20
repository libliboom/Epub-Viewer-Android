package com.github.libliboom.epubviewer.presentation.bookshelf

import com.github.libliboom.epubviewer.app.mvi.BaseStoreFactory
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.Action

class BookshelfPresenter {

  private val bookshelfStore = BookshelfStoreFactory(BaseStoreFactory()).create()
  val state = bookshelfStore.state

  init {
    bookshelfStore.init()
  }

  fun dispatch(action: Action) {
    bookshelfStore.dispatch(action)
  }
}
