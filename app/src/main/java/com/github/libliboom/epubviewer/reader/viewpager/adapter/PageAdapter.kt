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
import com.github.libliboom.epubviewer.util.file.EPubUtils
import com.github.libliboom.epubviewer.util.file.StorageManager
import kotlinx.android.synthetic.main.item_web_view.view.web_view
import kotlin.math.ceil

class PageAdapter(
    private val context: Context,
    private val viewModel: EPubReaderViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // TODO: 2020/06/05 move it to application level to enable only on debug mode
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
                setOnTouchListener { _, event -> event.action == MotionEvent.ACTION_MOVE }
                // based on page
                val pageInfo = viewModel.loadPageByIndex(
                    StorageManager.getExtractedPath(context),
                    position
                )
                holder.itemView.web_view.loadUrl(EPubUtils.getUri(pageInfo))
            } else {
                isScrollContainer = true
                setOnTouchListener { _, _ -> false }
                setOnScrollChangedCallback(object : ReaderWebView.OnScrollChangedCallback {
                    override fun onScrolledToTop() {
                        val pageInfo = viewModel.loadPreviousSpine(
                            StorageManager.getExtractedPath(context)
                        )
                        holder.itemView.web_view.loadUrl(EPubUtils.getUri(pageInfo))
                    }

                    override fun onScrolledToBottom() {
                        val pageInfo = viewModel.loadNextSpine(
                            StorageManager.getExtractedPath(context)
                        )
                        holder.itemView.web_view.loadUrl(EPubUtils.getUri(pageInfo))
                    }

                    private var pNth = -1
                    override fun onUpdatePage(nth: String) {
                        if (nth == "null") return
                        if (pNth == nth.toInt()) return
                        pNth = nth.toInt()
                        val newNth = ceil(nth.toDouble()).toInt()
                        viewModel.run {
                            unlockPaging()
                            updatePageIndex(
                                StorageManager.getExtractedPath(context),
                                url,
                                newNth
                            )
                        }
                    }
                })

                // base on spine
                val pageInfo = viewModel.loadSpineByIndex(
                    StorageManager.getExtractedPath(context), position
                )
                holder.itemView.web_view.loadUrl(EPubUtils.getUri(pageInfo))
            }
        }
    }

    override fun getItemId(position: Int) = position * 10L

    override fun getItemCount() = viewModel.getPageCount().value!!
}
