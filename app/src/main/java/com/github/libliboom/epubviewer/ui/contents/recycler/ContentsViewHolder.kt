package com.github.libliboom.epubviewer.ui.contents.recycler

import android.content.Context
import com.github.libliboom.epubviewer.app.ui.BaseViewHolder
import com.github.libliboom.epubviewer.databinding.ItemContentsBinding
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.Action.Ui
import com.github.libliboom.epubviewer.ui.contents.recycler.ContentsViewHolder.Contents

class ContentsViewHolder(
  private val binding: ItemContentsBinding,
  private val eventListener: EventListener
) : BaseViewHolder<ItemContentsBinding, Contents>(binding) {

  override fun init() {
    binding.contentsItemTv.setOnClickListener {
      val text = binding.contentsItemTv.text.toString()
      eventListener.invoke(Ui.ClickContents(text))
    }
  }

  override fun onBindItem(context: Context, item: Contents) {
    super.onBindItem(context, item)
    with(binding) {
      contentsItemTv.text = item.contents
      srcsItemTv.text = item.src
    }
  }

  data class Contents(val contents: String, val src: String) : BaseItem()
}
