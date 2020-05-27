package com.github.libliboom.epubviewer.reader.viewpager.adapter

import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.libliboom.epubviewer.reader.view.ReaderWebViewClient
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.epubviewer.reader.viewpager.viewholder.PageViewHolder
import kotlinx.android.synthetic.main.item_web_view.view.web_view

class PageAdapter(
    private val context: Context,
    private val viewModel: EPubReaderViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PageViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.web_view.apply {
            settings.javaScriptEnabled = true
            isScrollContainer = false
            webViewClient = ReaderWebViewClient(viewModel)
            setOnTouchListener { _, event ->  event.action == MotionEvent.ACTION_MOVE }
            tag = position
        }
        viewModel.loadPageByPageIndex(context, holder.itemView.web_view, position)
    }

    override fun getItemCount(): Int {
        return viewModel.getPageCount().value!!
    }
}