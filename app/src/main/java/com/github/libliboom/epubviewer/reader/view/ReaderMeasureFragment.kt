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
import com.github.libliboom.epubviewer.databinding.FragmentReaderMeaureBinding
import com.github.libliboom.epubviewer.db.preference.SettingsPreference
import com.github.libliboom.epubviewer.db.room.Book
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.epubviewer.util.file.EPubUtils
import com.github.libliboom.epubviewer.util.js.Js.calcPage4HorizontalJs
import com.github.libliboom.epubviewer.util.js.Js.calcPage4VerticalJs
import com.github.libliboom.epubviewer.util.js.Js.callColumns
import com.github.libliboom.epubviewer.util.js.Js.callPageCount
import com.github.libliboom.epubviewer.util.js.Js.columns4HorizontalJs
import com.github.libliboom.epubviewer.util.js.Js.setStyle
import com.github.libliboom.epubviewer.util.js.Js.sumPageCount
import com.github.libliboom.utils.io.FileUtils
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import java8.util.stream.StreamSupport
import org.json.JSONObject

class ReaderMeasureFragment : BaseFragment() {

    private val viewModel: EPubReaderViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(EPubReaderViewModel::class.java)
    }

    private val binding: FragmentReaderMeaureBinding by lazy {
        getBinding() as FragmentReaderMeaureBinding
    }

    override fun getLayoutId() = R.layout.fragment_reader_meaure

    // TODO: 2020/05/27 Bad smell...
    private var tot = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.measureSpinner.visibility = View.VISIBLE

        val f = arguments?.getStringArrayList(ARGS_FILE_LIST)!!
        val filelist = StreamSupport.stream(f)
            .map { f -> FileUtils.getFileName(f) }
            .distinct()
            .toArray()

        binding.measureWebView.apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(PageHelper(), "Android")
        }

        var idx = 0
        val len = filelist.size - 1
        binding.measureWebView.loadUrl(FileUtils.getFileUri(filelist[0] as String))
        Observable.create(RxWebViewWrapper(binding.measureWebView))
            .doOnError({})
            .subscribe({
                if (idx++ < len) {
                    binding.measureWebView.loadUrl(FileUtils.getFileUri(filelist[idx] as String))
                    viewModel.pages4ChapterByRendering.add(Pair(idx, tot))
                } else {
                    viewModel.pages4ChapterByRendering.add(Pair(idx, tot))
                    Handler().postDelayed({
                        viewModel.pageCountByRendering.value = tot + 1
                        insertBook()
                        requireActivity().onBackPressed()
                    }, 1000)
                }
            }, {
                viewModel.onBackPressed()
            })
    }

    private fun insertBook() {
        val m = EPubUtils.getMode(SettingsPreference.getViewMode(context))
        val map = viewModel.pages4ChapterByRendering
            .map { it.first.toString() to it.second.toString() }.toMap()
        val jsonMap = JSONObject(map).toString()
        val filename = arguments?.getString(ARGS_FILE_NAME)!!
        viewModel.insert(Book("$filename-$m", "${tot + 1}", jsonMap))
    }

    inner class PageHelper {
        @JavascriptInterface
        fun sumPageCount(page: String) {
            tot += page.toInt()
        }
    }

    companion object {
        const val ARGS_FILE_LIST = "contents_file_list"
        const val ARGS_FILE_NAME = "file_name"

        fun newInstance(files: ArrayList<String>, filename: String): ReaderMeasureFragment {
            val args = Bundle()
            args.putStringArrayList(ARGS_FILE_LIST, files)
            args.putString(ARGS_FILE_NAME, filename)

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
                view?.loadUrl(setStyle())
                val pageMode = SettingsPreference.getViewMode(webView.context)
                if (pageMode) {
                    view?.run {
                        loadUrl(columns4HorizontalJs())
                        loadUrl(calcPage4HorizontalJs())
                        evaluateJavascript(callColumns()) {
                            evaluateJavascript(callPageCount()) { page ->
                                loadUrl(sumPageCount(page))
                            }
                        }
                    }
                } else {
                    view?.run {
                        loadUrl(calcPage4VerticalJs())
                        evaluateJavascript(callPageCount()) { page ->
                            loadUrl(sumPageCount(page))
                        }
                    }
                }
                emitter.onNext(WebViewEvent.PageManager(url))
            }
        }
    }
}

sealed class WebViewEvent {
    data class PageManager(val url: String?) : WebViewEvent()
}
