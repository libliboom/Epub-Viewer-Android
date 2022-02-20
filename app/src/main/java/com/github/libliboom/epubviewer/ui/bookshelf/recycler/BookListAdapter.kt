package com.github.libliboom.epubviewer.ui.bookshelf.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.databinding.ItemBookBinding
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.Action
import com.github.libliboom.epubviewer.ui.bookshelf.BookshelfViewModel

typealias EventListener = (Action) -> Unit

class BookListAdapter(private val eventListener: EventListener) :
  RecyclerView.Adapter<BookListViewHolder>() {

  private var files = listOf<String>()
  private var covers = listOf<String>()

  // TODO: from di
  private lateinit var requestManager: RequestManager

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
    val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return BookListViewHolder(binding, eventListener).also { it.init() }
  }

  override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
    val book = BookListViewHolder.Book(requestManager, covers[position])
    holder.onBindItem(holder.itemView.context, book)
  }

  override fun getItemCount() = covers.size

  // TODO: DiffUtil
  fun updateViewModel(viewModel: BookshelfViewModel, requestManager: RequestManager) {
    files = viewModel.ePubFiles
    covers = viewModel.coverPaths
    this.requestManager = requestManager
  }
}
