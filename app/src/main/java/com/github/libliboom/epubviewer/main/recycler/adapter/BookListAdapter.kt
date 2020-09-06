package com.github.libliboom.epubviewer.main.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.databinding.ItemBookBinding
import com.github.libliboom.epubviewer.main.recycler.viewholder.BookListViewHolder
import com.github.libliboom.epubviewer.main.viewmodel.BookshelfViewModel
import io.reactivex.subjects.PublishSubject

// TODO: 2020/05/13 DI with properties
class BookListAdapter : RecyclerView.Adapter<BookListViewHolder>() {

    private var files = listOf<String>()
    private var covers = listOf<String>()
    private val publishSubject: PublishSubject<Int> = PublishSubject.create()

    private lateinit var requestManager: RequestManager
    private lateinit var binding: ItemBookBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemBookBinding.inflate(inflater, parent, false)
        return BookListViewHolder(binding)
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
