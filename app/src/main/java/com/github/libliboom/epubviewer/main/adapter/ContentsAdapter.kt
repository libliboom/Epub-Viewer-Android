package com.github.libliboom.epubviewer.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.libliboom.epubviewer.R
import kotlinx.android.synthetic.main.item_contents.view.tv_contents_item

class ContentsAdapter : RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsViewHolder {
        val view = LayoutInflater.from(parent.context).run {
            inflate(R.layout.item_contents, parent, false)
        }

        return ContentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentsViewHolder, position: Int) {
        holder.onBindItem(position)
    }

    override fun getItemCount() = CONTENTS_LIST.size

    inner class ContentsViewHolder(view: View) : RViewHolder(view) {

        private var contenstTv: TextView = view.tv_contents_item

        override fun onBindItem(position: Int) {
            contenstTv.text = CONTENTS_LIST.get(position)
        }
    }

    companion object {
        val CONTENTS_LIST = listOf(
            "Ch01",
            "Ch02",
            "Ch03",
            "Ch04",
            "Ch05",
            "Ch0",
            "Ch07",
            "Ch08",
            "Ch09",
            "Ch10"
        )
    }
}