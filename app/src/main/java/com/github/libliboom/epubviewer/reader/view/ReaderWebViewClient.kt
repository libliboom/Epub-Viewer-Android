package com.github.libliboom.epubviewer.reader.view

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
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

    private fun isWebProtocol(url: String) = StreamSupport.stream(webProtocols)
        .anyMatch { p -> url.startsWith(p) }

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