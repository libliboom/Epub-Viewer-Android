package com.github.libliboom.epubviewer.app.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<Binding : ViewBinding, Item>(binding: Binding) :
  RecyclerView.ViewHolder(binding.root) {

  protected var item: Item? = null

  abstract fun init()

  open fun onBindItem(context: Context, item: Item) {
    this.item = item
  }

  abstract class BaseItem
}
