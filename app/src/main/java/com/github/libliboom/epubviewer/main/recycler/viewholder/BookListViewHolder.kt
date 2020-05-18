package com.github.libliboom.epubviewer.main.recycler.viewholder

import android.content.Context
import android.view.View
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.base.BaseViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_book.view.image_view_book

class BookListViewHolder(private val view: View) : BaseViewHolder(view) {

    fun onBindSubject(publishSubject: PublishSubject<Int>) {
        getClickObservable().subscribe(publishSubject)
    }

    override fun onBindItem(context: Context, item: BaseItem) {
        item as Book
        view.image_view_book.apply {
            bindCover(item.requestManager, item.url)
        }
    }

    private fun getClickObservable(): Observable<Int> {
        return Observable.create { e ->
            view.image_view_book.setOnClickListener {
                e.onNext(adapterPosition)
            }
        }
    }

    private fun bindCover(requestManager: RequestManager, url: String) {
        requestManager
            .load(url)
            .into(view.image_view_book)
    }

    data class Book(val requestManager: RequestManager, val url: String) : BaseItem()
}