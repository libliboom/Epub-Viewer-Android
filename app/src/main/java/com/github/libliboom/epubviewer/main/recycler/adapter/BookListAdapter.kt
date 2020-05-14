package com.github.libliboom.epubviewer.main.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.recycler.viewholder.BookListViewHolder
import com.github.libliboom.epubviewer.main.viewmodel.BookshelfViewModel
import io.reactivex.subjects.PublishSubject

// TODO: 2020/05/13 DI with properties
class BookListAdapter : RecyclerView.Adapter<BookListViewHolder>() {

    private var mFiles = listOf<String>()
    private var mCovers = listOf<String>()
    private val mPublishSubject: PublishSubject<Int> = PublishSubject.create()

    private lateinit var mRequestManager: RequestManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        val view = LayoutInflater.from(parent.context).run {
            inflate(R.layout.item_book, parent, false)
        }

        return BookListViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        val book = BookListViewHolder.Book(mRequestManager, mCovers[position])
        holder.onBindSubject(mPublishSubject)
        holder.onBindItem(holder.itemView.context, book)
    }

    override fun getItemCount() = mCovers.size

    // REFACTORING: 2020/05/13 Not enough...
    fun updateViewModel(viewModel: BookshelfViewModel, requestManager: RequestManager) {
        mFiles = viewModel.ePubFiles
        mCovers = viewModel.coverPaths
        mRequestManager = requestManager
    }

    fun getPublishSubject(): PublishSubject<Int> {
        return mPublishSubject
    }
}
