package com.github.libliboom.epubviewer.reader.view

import android.webkit.WebView
import android.webkit.WebViewClient

class ReaderWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url)
        return true
    }
}