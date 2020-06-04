package com.github.libliboom.epubviewer.reader.viewpager.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.libliboom.epubviewer.R

class PageViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_web_view, parent, false)
)
