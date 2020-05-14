package com.github.libliboom.epubviewer.reader.view

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import com.github.libliboom.epubviewer.util.event.ClickUtils

class ReaderWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0
) : WebView(context, attrs, defStyleArr) {

    private val clickUtils = ClickUtils()

    private lateinit var listener: OnScrollChangedCallback

    fun setOnScrollChangedCallback(callback: OnScrollChangedCallback) {
        listener = callback
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)

        if (clickUtils.isLoadedOnce()) return

        if (clampedY) {
            if (scrollY == 0) {
                listener.onScrolledToTop()
            } else {
                listener.onScrolledToBottom()
            }
        }
    }

    interface OnScrollChangedCallback {
        fun onScrolledToTop()
        fun onScrolledToBottom()
    }
}