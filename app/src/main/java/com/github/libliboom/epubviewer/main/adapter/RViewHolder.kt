package com.github.libliboom.epubviewer.main.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class RViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBindItem(position: Int)
}
