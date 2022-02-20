package com.github.libliboom.epubviewer.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

  abstract fun onBindItem(context: Context, item: BaseItem)

  abstract class BaseItem
}
