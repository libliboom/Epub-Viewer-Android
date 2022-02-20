package com.github.libliboom.epubviewer.ui.bookshelf

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.libliboom.common.io.FileUtils
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfPresenter
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.Action
import com.github.libliboom.epubviewer.util.dev.EPubFileStub
import com.github.libliboom.epubviewer.util.file.StorageManager
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

// TODO: 2020/05/13 Fetch data form local database
class BookshelfViewModel : ViewModel() {

  private val presenter by lazy { BookshelfPresenter() }

  var ePubFiles = listOf<String>()

  val coverPaths = listOf(
    EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_01,
    EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_02,
    EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_03,
    EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_04
  )

  fun bind(binder: BookshelfViewBinder) {
    viewModelScope.launch {
      presenter.state.collect { binder.represent(it) }
    }
  }

  fun dispatch(action: Action) {
    presenter.dispatch(action)
  }

  fun initResources(context: Context) {
    ePubFiles = context.assets.list("books")?.toList() as List<String>
    forceInitialization(context)
  }

  private fun forceInitialization(context: Context) {
    val cachedBooksPath = createBooksDirIfNotExist(context)

    lateinit var istream: InputStream
    lateinit var ostream: OutputStream

    try {
      for (f in ePubFiles) {
        val assetsFile = EPubFileStub.BOOKS_PATH + f
        istream = context.assets.open(assetsFile)
        ostream = FileOutputStream(File(cachedBooksPath + f))
        FileUtils.copy(istream, ostream)
      }
    } finally {
      istream.close()
      ostream.close()
    }
  }

  private fun createBooksDirIfNotExist(context: Context): String {
    val cachedBooksPath = StorageManager.getBooksPath(context)
    val dir = File(cachedBooksPath)
    if (dir.exists().not()) dir.mkdirs()
    return cachedBooksPath
  }
}
