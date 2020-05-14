package com.github.libliboom.epubviewer.reader.fragment

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
import com.github.libliboom.utils.io.FileUtils
import kotlinx.android.synthetic.main.fragment_epub_reader.bottom_nv_reader
import kotlinx.android.synthetic.main.fragment_epub_reader.web_view

class EPubReaderFragment : BaseFragment(), ReaderWebView.OnScrollChangedCallback {

    // TODO: 2020/05/14 to Rx
    private val clickUtils = ClickUtils()

    private val mViewModel: EPubReaderViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(EPubReaderViewModel::class.java)
    }

    override fun getLayoutId() = R.layout.fragment_epub_reader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()

        mViewModel.initEpub(requireContext())
        loadChapter(mViewModel.currentChapter)

        web_view.setOnScrollChangedCallback(this)
        web_view.settings.javaScriptEnabled = true // for external link
    }

    private fun setupBottomNavigation() {
        bottom_nv_reader.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_contents -> mViewModel.startContentsActivity(requireActivity())
                R.id.nav_settings -> mViewModel.startSettingActivity(requireActivity())
                else -> Toast.makeText(requireActivity(), "Do nothing", Toast.LENGTH_SHORT).show()
            }

            true
        }
    }

    private fun loadChapter(next: Int) {
        val page = mViewModel.getPath(requireContext(), next)

        web_view.run {
            webViewClient = ReaderWebViewClient()
            loadUrl(FileUtils.getFileUri(page))
        }
    }

    override fun onScrolledToTop() {
        if (clickUtils.isLoadedOnce()) return
        loadChapter(mViewModel.currentChapter - 1)
    }

    override fun onScrolledToBottom() {
        if (clickUtils.isLoadedOnce()) return
        loadChapter(mViewModel.currentChapter + 1)
    }

    companion object {
        fun newInstance() = EPubReaderFragment()
    }
}