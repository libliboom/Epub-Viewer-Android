package com.github.libliboom.epubviewer.domain.home.usecase

import com.github.libliboom.epubviewer.domain.home.Book
import com.github.libliboom.epubviewer.domain.home.Section

data class GridSection(
  val name: String,
  val books: List<Book>
) : Section
