package com.github.libliboom.epubviewer.ui.viewer

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import com.github.libliboom.common.io.FileUtils
import com.github.libliboom.epubviewer.app.ui.BaseFragment
import com.github.libliboom.epubviewer.databinding.FragmentReaderMeaureBinding
import com.github.libliboom.epubviewer.datasource.settings.SettingsPreference
import com.github.libliboom.epubviewer.db.room.Book
import com.github.libliboom.epubviewer.util.file.EPubUtils
import com.github.libliboom.epubviewer.util.js.Js.calcPage4HorizontalJs
import com.github.libliboom.epubviewer.util.js.Js.calcPage4VerticalJs
import com.github.libliboom.epubviewer.util.js.Js.callColumns
import com.github.libliboom.epubviewer.util.js.Js.callPageCount
import com.github.libliboom.epubviewer.util.js.Js.columns4HorizontalJs
import com.github.libliboom.epubviewer.util.js.Js.setStyle
import com.github.libliboom.epubviewer.util.js.Js.sumPageCount
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import java8.util.stream.StreamSupport
import org.json.JSONObject

class ReaderMeasureFragment : BaseFragment<FragmentReaderMeaureBinding>() {

  private val viewModel: EPubReaderViewModel by activityViewModels()

  override fun inflateBinding(container: ViewGroup?) =
    FragmentReaderMeaureBinding.inflate(layoutInflater, container, false)

  // TODO: 2020/05/27 Bad smell...
  private var tot = 0

  override fun afterInitView(binding: FragmentReaderMeaureBinding) {
    super.afterInitView(binding)
    binding.measureSpinner.visibility = View.VISIBLE

    val f = arguments?.getStringArray(ARGS_FILE_LIST)?.toList() as List<String>
    val filelist = StreamSupport.stream(f)
      .map { file -> FileUtils.getFileNameFromUri(file) }
      .distinct()
      .toArray()

    binding.measureWebView.apply {
      settings.javaScriptEnabled = true
      addJavascriptInterface(PageHelper(), "Android")
    }

    var idx = 0
    val len = filelist.size - 1
    // REVIEW: 2020/06/05 Hot observable
    binding.measureWebView.loadUrl(FileUtils.getFileUri(filelist[0] as String))
    Observable.create(RxWebViewWrapper(binding.measureWebView))
      .doOnError {}
      .subscribe(
        {
          if (idx++ < len) {
            binding.measureWebView.loadUrl(FileUtils.getFileUri(filelist[idx] as String))
            viewModel.pages4ChapterByRendering.add(Pair(idx, tot))
          } else {
            viewModel.pages4ChapterByRendering.add(Pair(idx, tot))
            Handler().postDelayed(
              {
                viewModel.pageCountByRendering.value = tot + 1
                insertBook()
                requireActivity().onBackPressed()
              },
              1000
            )
          }
        },
        {
          viewModel.onBackPressed()
        }
      )
  }

  private fun insertBook() {
    val m = EPubUtils.getMode(SettingsPreference.getViewMode())
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

    fun newInstance(files: List<String>, filename: String): ReaderMeasureFragment {
      val args = Bundle()
      args.putStringArray(ARGS_FILE_LIST, files.toTypedArray())
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
        val pageMode = SettingsPreference.getViewMode()
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
