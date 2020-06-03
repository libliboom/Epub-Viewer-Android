package com.github.libliboom.epubviewer.reader.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.libliboom.epubviewer.db.preference.SettingsPreference
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.epubviewer.util.file.EPubUtils
import com.github.libliboom.epubviewer.util.file.EPubUtils.DELIMITER_NTH
import com.github.libliboom.epubviewer.util.js.Js.callColumns
import com.github.libliboom.epubviewer.util.js.Js.callLoad
import com.github.libliboom.epubviewer.util.js.Js.callNth
import com.github.libliboom.epubviewer.util.js.Js.columns4HorizontalJs
import com.github.libliboom.epubviewer.util.js.Js.getHNthJs
import com.github.libliboom.epubviewer.util.js.Js.getNthJs
import com.github.libliboom.epubviewer.util.js.Js.loadJs
import com.github.libliboom.epubviewer.util.js.Js.setStyle
import java8.util.stream.StreamSupport

// REFACTORING: 2020/05/18 init with dagger
class ReaderWebViewClient(private val viewModel: EPubReaderViewModel) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view ?: return true
        url ?: return true

        if (isWebProtocol(url)) {
            launchBrowser(url, view)
        } else {
            val uri = parseUri(url!!)
            if (url.split("#").size == 2) {
                view.loadUrl(uri)
            } else {
                loadChapter(view, uri)
            }
        }
        return true
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, parseUri(url!!), favicon)
        view?.loadUrl(setStyle())
        view?.loadUrl(loadJs())
        view?.loadUrl(getNthJs())
        view?.loadUrl(getHNthJs())
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, parseUri(url!!))
        view?.loadUrl(setStyle())

        if (SettingsPreference.getViewMode(view?.context)) {
            setHorizontalMode(view, url)
        } else {
            setVerticalMode(url, view)
        }
    }

    private fun setHorizontalMode(view: WebView?, url: String) {
        view?.run {
            loadUrl(columns4HorizontalJs())
            if (isTurningPage(url)) {
                evaluateJavascript(callColumns()) {
                    scrollTo((parseNth(url!!).toInt()) * getWidth(view), 0)
                }
            } else {
                evaluateJavascript(callColumns()) {
                    view?.evaluateJavascript(callNth()) { n ->
                        val nth = n.toInt() - 1  // workaround
                        viewModel.updatePageIndex(view.context, url, nth)
                    }
                }
            }
        }
    }

    private fun setVerticalMode(url: String, view: WebView?) {
        if (isTurningPage(url)) { // turningSpine
            val nth = parseNth(url)
            view?.evaluateJavascript(callLoad(nth)) {
                viewModel.updatePageIndex(view.context, url, nth.toInt())
            }
        } else {
            view?.evaluateJavascript(callNth()) { n ->
                viewModel.updatePageIndex(view.context, url, n.toInt())
            }
        }
    }

    private fun isWebProtocol(url: String) = StreamSupport.stream(webProtocols)
        .anyMatch { p -> url.startsWith(p) }

    private fun parseUri(url: String): String = url.split(EPubUtils.DELIMITER_NTH)[0]

    private fun parseNth(url: String): String = url.split(EPubUtils.DELIMITER_NTH)[1]

    private fun launchBrowser(url: String?, view: WebView) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        view.context.startActivity(i)
    }

    private fun loadChapter(view: WebView, url: String) {
        viewModel.run{
            updateChapterIndex(url)
            loadChapterByUrl(view.context, view, url)
        }
    }

    private fun isTurningPage(url: String) = url.split(DELIMITER_NTH).size == 2

    private fun getWidth(view: WebView) = (view.width + 3) // workaround

    companion object {
        val webProtocols = listOf("https://", "http://")
    }
}