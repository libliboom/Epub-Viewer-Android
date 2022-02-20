package com.github.libliboom.epubviewer.ui.contents.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.libliboom.epubviewer.R
import io.reactivex.subjects.PublishSubject

class ContentsAdapter : RecyclerView.Adapter<ContentsViewHolder>() {

  private val publishSubject: PublishSubject<String> = PublishSubject.create()

  private lateinit var cover: String

  private lateinit var contentsList: List<String>
  private lateinit var srcs: List<String>

  // REFACTORING: 2020/05/18 with dagger
  fun init(cover: String, contentsList: List<String>, srcs: List<String>) {
    this.cover = cover
    this.contentsList = contentsList
    this.srcs = srcs
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsViewHolder {
    val view = LayoutInflater.from(parent.context).run {
      inflate(R.layout.item_contents, parent, false)
    }

    return ContentsViewHolder(view)
  }

  override fun onBindViewHolder(holder: ContentsViewHolder, position: Int) {
    val contents = ContentsViewHolder.Contents(contentsList[position], srcs[position])
    holder.onBindSubject(publishSubject)
    holder.onBindItem(holder.itemView.context, contents)
  }

  override fun getItemCount() = contentsList.size

  fun getPublishSubject(): PublishSubject<String> {
    return publishSubject
  }
}
