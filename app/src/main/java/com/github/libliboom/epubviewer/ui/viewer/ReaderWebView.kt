package com.github.libliboom.epubviewer.ui.viewer

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import com.github.libliboom.epubviewer.db.preference.SettingsPreference
import com.github.libliboom.epubviewer.util.event.ClickUtils
import com.github.libliboom.epubviewer.util.js.Js.callNth

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

  override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
    super.onScrollChanged(l, t, oldl, oldt)

    if (SettingsPreference.getViewMode(context)) return

    evaluateJavascript(callNth()) { nth ->
      nth?.let { listener.onUpdatePage(nth) }
    }
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
    fun onUpdatePage(nth: String)
  }
}
