package com.github.libliboom.epubviewer.reader.view

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseFragment
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.utils.io.FileUtils
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import kotlinx.android.synthetic.main.fragment_reader_meaure.spinner
import kotlinx.android.synthetic.main.fragment_reader_meaure.web_view_measure

class ReaderMeasureFragment : BaseFragment() {

    private val viewModel: EPubReaderViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(EPubReaderViewModel::class.java)
    }

    override fun getLayoutId() = R.layout.fragment_reader_meaure

    // TODO: 2020/05/27 Bad smell...
    private var tot = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner.visibility = View.VISIBLE

        val filelist = arguments?.getStringArrayList(ARGS_FILE_LIST)!!
        web_view_measure.settings.javaScriptEnabled = true
        web_view_measure.addJavascriptInterface(PageHelper(), "Android")

        var idx = 0
        val len = filelist.size - 1
        web_view_measure.loadUrl(FileUtils.getFileUri(filelist[0]))
        Observable.create(RxWebViewWrapper(web_view_measure))
            .subscribe {
                if (idx++ < len) {
                    web_view_measure.loadUrl(FileUtils.getFileUri(filelist[idx]))
                    viewModel.pages4ChapterByRendering.add(Pair(idx, tot++))
                } else {
                    viewModel.pages4ChapterByRendering.add(Pair(idx, tot++))
                    Handler().postDelayed({
                        viewModel.pageCountByRendering.value = tot
                        requireActivity().onBackPressed()
                    }, 1000)
                }
            }
    }

    inner class PageHelper {
        @JavascriptInterface
        fun printPageCount(page: String) {
            tot += page.toInt()
        }
    }

    companion object {
        const val ARGS_FILE_LIST = "contents_file_list"

        fun newInstance(files: ArrayList<String>): ReaderMeasureFragment {
            val args = Bundle()
            args.putStringArrayList(ARGS_FILE_LIST, files)

            val fragment = ReaderMeasureFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

class RxWebViewWrapper(private val webView: WebView) : ObservableOnSubscribe<WebViewEvent> {
    override fun subscribe(emitter: ObservableEmitter<WebViewEvent>) {
        webView.webViewClient = object : WebViewClient() {
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

                emitter.onNext(WebViewEvent.PageManager(url))
            }
        }
    }
}

sealed class WebViewEvent {
    data class PageManager(val url: String?) : WebViewEvent()
}
