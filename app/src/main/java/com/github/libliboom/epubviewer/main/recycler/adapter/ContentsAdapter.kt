package com.github.libliboom.epubviewer.main.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.recycler.viewholder.ContentsViewHolder
import io.reactivex.subjects.PublishSubject

class ContentsAdapter : RecyclerView.Adapter<ContentsViewHolder>() {

    private lateinit var mCover: String

    private lateinit var mContentsList: ArrayList<String>

    private val mPublishSubject: PublishSubject<Int> = PublishSubject.create()

    // REFACTORING: 2020/05/18 with dagger
    fun init(cover: String, contentsList: ArrayList<String>) {
        mCover = cover
        mContentsList = contentsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsViewHolder {
        val view = LayoutInflater.from(parent.context).run {
            inflate(R.layout.item_contents, parent, false)
        }

        return ContentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentsViewHolder, position: Int) {
        val contents = ContentsViewHolder.Contents(mContentsList[position])
        holder.onBindSubject(mPublishSubject)
        holder.onBindItem(holder.itemView.context, contents)
    }

    override fun getItemCount() = mContentsList.size

    fun getPublishSubject(): PublishSubject<Int> {
        return mPublishSubject
    }
}