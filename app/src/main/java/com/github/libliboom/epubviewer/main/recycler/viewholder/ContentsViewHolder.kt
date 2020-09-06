package com.github.libliboom.epubviewer.main.recycler.viewholder

import android.content.Context
import com.github.libliboom.epubviewer.base.BaseViewHolder
import com.github.libliboom.epubviewer.databinding.ItemContentsBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ContentsViewHolder(private val binding: ItemContentsBinding) : BaseViewHolder(binding.root) {

    fun onBindSubject(publishSubject: PublishSubject<String>) {
        getClickObservable().subscribe(publishSubject)
    }

    override fun onBindItem(context: Context, item: BaseItem) {
        item as Contents
        binding.contentsItem.text = item.contents
        binding.srcsItem.text = item.src
    }

    private fun getClickObservable(): Observable<String> {
        return Observable.create { event ->
            binding.contentsItem.setOnClickListener {
                event.onNext(binding.srcsItem.text.toString())
            }
        }
    }

    data class Contents(val contents: String, val src: String) : BaseItem()
}
