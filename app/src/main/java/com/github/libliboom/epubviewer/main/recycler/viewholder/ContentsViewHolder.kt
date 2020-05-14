package com.github.libliboom.epubviewer.main.recycler.viewholder

import android.content.Context
import android.view.View
import android.widget.TextView
import com.github.libliboom.epubviewer.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_contents.view.tv_contents_item

class ContentsViewHolder(view: View) : BaseViewHolder(view) {

    private var contentsTv: TextView = view.tv_contents_item

    override fun onBindItem(context: Context, item: BaseItem) {
        item as Contents
        contentsTv.text = item.contents
    }

    fun bindEtc(href: String) {

    }

    data class Contents(val contents: String) : BaseItem()

    companion object {
        val CONTENTS_LIST = listOf(
            "Ch01",
            "Ch02",
            "Ch03",
            "Ch04",
            "Ch05",
            "Ch06",
            "Ch07",
            "Ch08",
            "Ch09",
            "Ch10"
        )
    }
}