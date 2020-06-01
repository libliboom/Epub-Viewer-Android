package com.github.libliboom.epubviewer.reader.viewpager.adapter

import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.WebView
import androidx.recyclerview.widget.RecyclerView
import com.github.libliboom.epubviewer.db.preference.SettingsPreference
import com.github.libliboom.epubviewer.reader.view.ReaderWebView
import com.github.libliboom.epubviewer.reader.view.ReaderWebViewClient
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.epubviewer.reader.viewpager.viewholder.PageViewHolder
import kotlinx.android.synthetic.main.item_web_view.view.web_view
import kotlin.math.ceil

class PageAdapter(
    private val context: Context,
    private val viewModel: EPubReaderViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        WebView.setWebContentsDebuggingEnabled(true)
        val holder = PageViewHolder(parent)
        holder.setIsRecyclable(false)
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.web_view.apply {
            settings.javaScriptEnabled = true
            webViewClient = ReaderWebViewClient(viewModel)
            val pageMode = SettingsPreference.getViewMode(context)
            if (pageMode) {
                isScrollContainer = false
                setOnTouchListener { _, event ->  event.action == MotionEvent.ACTION_MOVE }
                // based on page
                viewModel.loadPageByIndex(context, holder.itemView.web_view, position)
            } else {
                isScrollContainer = true
                setOnTouchListener { _, _ ->  false }
                setOnScrollChangedCallback(object : ReaderWebView.OnScrollChangedCallback {
                    override fun onScrolledToTop() {
                        viewModel.loadPreviousSpine(context, holder.itemView.web_view)
                    }

                    override fun onScrolledToBottom() {
                        viewModel.loadNextSpine(context, holder.itemView.web_view)
                    }

                    private var pNth = -1
                    override fun onUpdatePage(nth: String) {
                        if (nth == "null") return
                        if (pNth == nth.toInt()) return
                        val newNth = ceil(nth.toDouble()).toInt()
                        viewModel.run {
                            unlockPaging()
                            updatePageIndex(context, url, newNth)
                        }
                        pNth = nth.toInt()
                    }
                })

                //base on spine
                viewModel.loadSpineByIndex(context, holder.itemView.web_view, position)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position * 10L
    }

    override fun getItemCount(): Int {
        return viewModel.getPageCount().value!!
    }
}