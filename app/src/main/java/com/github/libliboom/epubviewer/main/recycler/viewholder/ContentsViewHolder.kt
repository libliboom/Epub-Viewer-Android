package com.github.libliboom.epubviewer.main.recycler.viewholder

import android.content.Context
import android.view.View
import com.github.libliboom.epubviewer.base.BaseViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_contents.view.tv_contents_item

class ContentsViewHolder(private val view: View) : BaseViewHolder(view) {

    fun onBindSubject(publishSubject: PublishSubject<Int>) {
        getClickObservable().subscribe(publishSubject)
    }

    override fun onBindItem(context: Context, item: BaseItem) {
        item as Contents
        view.tv_contents_item.text = item.contents
    }

    private fun getClickObservable(): Observable<Int> {
        return Observable.create { e ->
            view.tv_contents_item.setOnClickListener {
                e.onNext(adapterPosition)
            }
        }
    }

    data class Contents(val contents: String) : BaseItem()
}