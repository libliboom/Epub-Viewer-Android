package com.github.libliboom.epubviewer.reader.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.epubviewer.util.file.EPubUtils
import java8.util.stream.StreamSupport

// REFACTORING: 2020/05/18 init with dagger
class ReaderWebViewClient(private val viewModel: EPubReaderViewModel) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view ?: return true
        url ?: return true

        if (isWebProtocol(url)) {
            launchBrowser(url, view)
        } else {
            loadChapter(view, url)
        }
        return true
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, parseUri(url!!), favicon)

        val loadJs = "javascript:function load(page) {" +
            "var height = window.innerHeight; " +
            "window.scrollTo(0, height*page);" +
            "}"

        view?.loadUrl(loadJs)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, parseUri(url!!))
        view?.evaluateJavascript("javascript:load(${parseNth(url!!)})") {}
    }

    private fun isWebProtocol(url: String) = StreamSupport.stream(webProtocols)
        .anyMatch { p -> url.startsWith(p) }

    private fun parseUri(url: String): String = url.split("#")[0]

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

    companion object {
        val webProtocols = listOf("https://", "http://")
    }
}