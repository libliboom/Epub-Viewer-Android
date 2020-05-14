package com.github.libliboom.epubviewer.main.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.recycler.viewholder.ContentsViewHolder

class ContentsAdapter : RecyclerView.Adapter<ContentsViewHolder>() {

    private lateinit var mHref: String

    private lateinit var mContentsList: ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsViewHolder {
        val view = LayoutInflater.from(parent.context).run {
            inflate(R.layout.item_contents, parent, false)
        }

        return  ContentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentsViewHolder, position: Int) {
        val contents = ContentsViewHolder.Contents(mContentsList[position])
        holder.bindEtc(mHref)
        holder.onBindItem(holder.itemView.context, contents)
    }

    override fun getItemCount() = mContentsList.size

    fun update(href: String) {
        mHref = href
    }

    fun update(contentsList: ArrayList<String>) {
        mContentsList = contentsList
    }
}