package com.github.libliboom.epubviewer.ui.contents.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.libliboom.epubviewer.databinding.ItemContentsBinding
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore

typealias EventListener = (ContentsStore.Action) -> Unit

class ContentsAdapter(private val eventListener: EventListener) :
  RecyclerView.Adapter<ContentsViewHolder>() {

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
    val binding = ItemContentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ContentsViewHolder(binding, eventListener).also { it.init() }
  }

  override fun onBindViewHolder(holder: ContentsViewHolder, position: Int) {
    val contents = ContentsViewHolder.Contents(contentsList[position], srcs[position])
    holder.onBindItem(holder.itemView.context, contents)
  }

  override fun getItemCount() = contentsList.size
}
