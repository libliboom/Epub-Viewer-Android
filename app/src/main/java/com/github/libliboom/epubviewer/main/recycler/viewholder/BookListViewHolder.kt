package com.github.libliboom.epubviewer.main.recycler.viewholder

import android.content.Context
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.base.BaseViewHolder
import com.github.libliboom.epubviewer.databinding.ItemBookBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class BookListViewHolder(private val binding: ItemBookBinding) : BaseViewHolder(binding.root) {

    fun onBindSubject(publishSubject: PublishSubject<Int>) {
        getClickObservable().throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe(publishSubject)
    }

    override fun onBindItem(context: Context, item: BaseItem) {
        item as Book
        binding.imageViewBook.apply {
            bindCover(item.requestManager, item.url)
        }
    }

    private fun getClickObservable(): Observable<Int> {
        return Observable.create { event ->
            binding.imageViewBook.setOnClickListener {
                event.onNext(adapterPosition)
            }
        }
    }

    private fun bindCover(requestManager: RequestManager, url: String) {
        requestManager
            .load(url)
            .into(binding.imageViewBook)
    }

    data class Book(val requestManager: RequestManager, val url: String) : BaseItem()
}
