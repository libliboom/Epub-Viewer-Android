package com.github.libliboom.epubviewer.db.room

import androidx.lifecycle.LiveData

class BookRepository(private val bookDao: BookDao) {

  val allBooks: LiveData<List<Book>> = bookDao.getAllBooks()

  suspend fun insert(book: Book) {
    bookDao.insert(book)
  }

  suspend fun delete(book: Book) {
    bookDao.delete(book)
  }
}
