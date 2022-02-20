package com.github.libliboom.epubviewer.ui.bookshelf.recycler

import android.content.Context
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.app.ui.BaseViewHolder
import com.github.libliboom.epubviewer.databinding.ItemBookBinding
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore

class BookListViewHolder(
  private val binding: ItemBookBinding,
  private val eventListener: EventListener
) : BaseViewHolder<ItemBookBinding, BookListViewHolder.Book>(binding) {

  override fun init() {
    binding.imageViewBook.setOnClickListener {
      eventListener.invoke(BookshelfStore.Action.Ui.ClickBook(adapterPosition))
    }
  }

  override fun onBindItem(context: Context, item: Book) {
    super.onBindItem(context, item)
    binding.imageViewBook.apply {
      bindCover(item.requestManager, item.url)
    }
  }

  private fun bindCover(requestManager: RequestManager, url: String) {
    requestManager
      .load(url)
      .into(binding.imageViewBook)
  }

  data class Book(val requestManager: RequestManager, val url: String) : BaseItem()
}
