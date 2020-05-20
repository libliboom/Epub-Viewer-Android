package com.github.libliboom.epubviewer.reader.fragment

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseFragment
import com.github.libliboom.epubviewer.reader.view.ReaderWebView
import com.github.libliboom.epubviewer.reader.view.ReaderWebViewClient
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.epubviewer.util.event.ClickUtils
import kotlinx.android.synthetic.main.fragment_epub_reader.bottom_nv_reader
import kotlinx.android.synthetic.main.fragment_epub_reader.bottom_nv_seek_bar
import kotlinx.android.synthetic.main.fragment_epub_reader.web_view

class EPubReaderFragment : BaseFragment(), ReaderWebView.OnScrollChangedCallback {

    // TODO: 2020/05/14 to Rx
    private val clickUtils = ClickUtils()

    private val viewModel: EPubReaderViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(EPubReaderViewModel::class.java)
    }

    override fun getLayoutId() = R.layout.fragment_epub_reader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()

        viewModel.run {
            initEpub(requireContext())
            loadChapterByPageIndex(requireContext(), web_view, viewModel.currentChapterIdx)
        }

        bottom_nv_seek_bar.apply {
            progress = 100
            max = viewModel.ePub.pagination.pageCount
            setOnSeekBarChangeListener(mSeekBarChangeListener)
        }

        web_view.apply {
            settings.javaScriptEnabled = true
            webViewClient = ReaderWebViewClient(viewModel)
            setOnScrollChangedCallback(this@EPubReaderFragment)
        }
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

    // FIXME: 2020/05/19 prevent minus progress
    private val mSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            Toast.makeText(context, "${seekBar?.progress}", Toast.LENGTH_SHORT).show()
            viewModel.loadPageByPageIndex(requireContext(), web_view, seekBar?.progress!!)
        }
    }

    companion object {
        fun newInstance() = EPubReaderFragment()
    }
}