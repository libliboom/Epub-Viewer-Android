package com.github.libliboom.epubviewer.main.recycler.viewholder

import android.content.Context
import android.view.View
import com.github.libliboom.epubviewer.base.BaseViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_contents.view.tv_contents_item
import kotlinx.android.synthetic.main.item_contents.view.tv_srcs_item

class ContentsViewHolder(private val view: View) : BaseViewHolder(view) {

    fun onBindSubject(publishSubject: PublishSubject<String>) {
        getClickObservable().subscribe(publishSubject)
    }

    override fun onBindItem(context: Context, item: BaseItem) {
        item as Contents
        view.tv_contents_item.text = item.contents
        view.tv_srcs_item.text = item.src
    }

    private fun getClickObservable(): Observable<String> {
        return Observable.create { event ->
            view.tv_contents_item.setOnClickListener {
                event.onNext(view.tv_srcs_item.text.toString())
            }
        }
    }

    data class Contents(val contents: String, val src: String) : BaseItem()
}
