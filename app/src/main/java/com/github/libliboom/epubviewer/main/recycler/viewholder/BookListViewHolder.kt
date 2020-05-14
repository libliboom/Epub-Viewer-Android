package com.github.libliboom.epubviewer.main.recycler.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.github.libliboom.epubviewer.base.BaseViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_book.view.image_view_book

class BookListViewHolder(view: View) : BaseViewHolder(view) {

    private var bookIv: ImageView = view.image_view_book

    fun onBindSubject(publishSubject: PublishSubject<Int>) {
        getClickObservable().subscribe(publishSubject)
    }

    override fun onBindItem(context: Context, item: BaseItem) {
        item as Book
        bookIv.apply {
            bindCover(item.requestManager, item.url)
        }
    }

    private fun getClickObservable(): Observable<Int> {
        return Observable.create { e -> bookIv.setOnClickListener { e.onNext(adapterPosition) } }
    }

    private fun bindCover(requestManager: RequestManager, url: String) {
        requestManager
            .load(url)
            .into(bookIv)
    }

    data class Book(val requestManager: RequestManager, val url: String) : BaseItem()
}