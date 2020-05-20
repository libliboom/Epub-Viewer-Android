package com.github.libliboom.epubviewer.reader.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseFragment
import com.github.libliboom.epubviewer.reader.view.ReaderWebView
import com.github.libliboom.epubviewer.reader.view.ReaderWebViewClient
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.epubviewer.util.event.ClickUtils
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.jakewharton.rxbinding2.widget.SeekBarStopChangeEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_epub_reader.bottom_nv_reader
import kotlinx.android.synthetic.main.fragment_epub_reader.bottom_nv_seek_bar
import kotlinx.android.synthetic.main.fragment_epub_reader.tv_page_info
import kotlinx.android.synthetic.main.fragment_epub_reader.web_view

class EPubReaderFragment : BaseFragment(), ReaderWebView.OnScrollChangedCallback {

    // TODO: 2020/05/14 to Rx
    private val clickUtils = ClickUtils()

    private val viewModel: EPubReaderViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(EPubReaderViewModel::class.java)
    }

    override fun getLayoutId() = R.layout.fragment_epub_reader

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()

        viewModel.run {
            initEpub(requireContext())
            loadChapterByPageIndex(requireContext(), web_view, viewModel.currentChapterIdx)
        }

        bottom_nv_seek_bar.apply {
            progress = 0
            max = viewModel.ePub.pagination.pageCount
        }

        web_view.apply {
            settings.javaScriptEnabled = true
            webViewClient = ReaderWebViewClient(viewModel)
            setOnScrollChangedCallback(this@EPubReaderFragment)
        }

        setupSeekBar()
    }

    private fun setupBottomNavigation() {
        bottom_nv_reader.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_contents -> viewModel.startContentsActivity(requireActivity())
                R.id.nav_settings -> viewModel.startSettingActivity(requireActivity())
                else -> Toast.makeText(requireActivity(), "Do nothing", Toast.LENGTH_SHORT).show()
            }

            true
        }
    }

    private fun setupSeekBar() {
        tv_page_info.text = "${bottom_nv_seek_bar.progress}/${bottom_nv_seek_bar.max}"

        RxSeekBar.changeEvents(bottom_nv_seek_bar)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { seekBarChangeEvent ->
                when (seekBarChangeEvent) {
                    is SeekBarStopChangeEvent -> {
                        tv_page_info.text =
                            "${bottom_nv_seek_bar.progress}/${bottom_nv_seek_bar.max}"
                        viewModel.loadPageByPageIndex(
                            requireContext(),
                            web_view,
                            bottom_nv_seek_bar.progress!!
                        )
                    }
                }
            }
    }

    override fun onScrolledToTop() {
        if (clickUtils.isLoadedOnce()) return
        viewModel.loadChapterByPageIndex(requireContext(), web_view, viewModel.currentChapterIdx - 1)
    }

    override fun onScrolledToBottom() {
        if (clickUtils.isLoadedOnce()) return
        viewModel.loadChapterByPageIndex(requireContext(), web_view, viewModel.currentChapterIdx + 1)
    }

    fun loadSpecificChapter(chapter: Int) {
        viewModel.loadChapterByPageIndex(requireContext(), web_view, chapter)
    }

    companion object {
        fun newInstance() = EPubReaderFragment()
    }
}