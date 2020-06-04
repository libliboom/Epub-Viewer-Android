package com.github.libliboom.epubviewer.reader.view

import android.webkit.WebView
import android.webkit.WebViewClient

class MeasureWebViewClient : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        val calcPageJs = "javascript:function getPageCount() {" +
            "var d = document.getElementsByTagName('body')[0];" +
            "var innerH = window.innerHeight; " +
            "var fullH = d.offsetHeight; " +
            "var pageCount = Math.floor(fullH/innerH)+1;" +
            "return pageCount;" +
            "}"
        view?.loadUrl(calcPageJs)
        view?.evaluateJavascript("javascript:getPageCount()") { page ->
            view?.loadUrl("javascript:Android.printPageCount($page)")
        }
    }
}
