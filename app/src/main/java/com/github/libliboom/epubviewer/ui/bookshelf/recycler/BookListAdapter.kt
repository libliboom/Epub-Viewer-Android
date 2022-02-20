package com.github.libliboom.epubviewer.ui.bookshelf.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.ui.bookshelf.BookshelfViewModel
import io.reactivex.subjects.PublishSubject

// TODO: 2020/05/13 DI with properties
class BookListAdapter : RecyclerView.Adapter<BookListViewHolder>() {

  private var files = listOf<String>()
  private var covers = listOf<String>()
  private val publishSubject: PublishSubject<Int> = PublishSubject.create()

  private lateinit var requestManager: RequestManager

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
    val view = LayoutInflater.from(parent.context).run {
      inflate(R.layout.item_book, parent, false)
    }

    return BookListViewHolder(view)
  }

  override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
    val book = BookListViewHolder.Book(requestManager, covers[position])
    holder.onBindSubject(publishSubject)
    holder.onBindItem(holder.itemView.context, book)
  }

  override fun getItemCount() = covers.size

  // REFACTORING: 2020/05/13 Not enough...
  fun updateViewModel(viewModel: BookshelfViewModel, requestManager: RequestManager) {
    files = viewModel.ePubFiles
    covers = viewModel.coverPaths
    this.requestManager = requestManager
  }

  fun getPublishSubject(): PublishSubject<Int> {
    return publishSubject
  }
}
