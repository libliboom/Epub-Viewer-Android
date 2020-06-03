package com.github.libliboom.epubviewer.reader.fragment

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseFragment
import com.github.libliboom.epubviewer.db.preference.SettingsPreference
import com.github.libliboom.epubviewer.reader.view.ReaderWebView
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.epubviewer.reader.viewpager.adapter.PageAdapter
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.jakewharton.rxbinding2.widget.SeekBarStopChangeEvent
import com.jakewharton.rxbinding4.viewpager2.pageSelections
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_epub_reader.bottom_nv_reader
import kotlinx.android.synthetic.main.fragment_epub_reader.bottom_nv_seek_bar
import kotlinx.android.synthetic.main.fragment_epub_reader.tv_page_info
import kotlinx.android.synthetic.main.fragment_epub_reader.view_pager
import kotlinx.android.synthetic.main.item_web_view.view.web_view

class EPubReaderFragment : BaseFragment() {

    private val viewModel: EPubReaderViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(EPubReaderViewModel::class.java)
    }

    override fun getLayoutId() = R.layout.fragment_epub_reader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()
        load()
    }

    private fun load() {
        viewModel.run {
            viewModel.pageLock = true
            initDatabase(requireContext(), requireActivity())
            initEpub(requireContext())
            calcPageIfNotCached()
            pageCountByRendering.observe(requireActivity(),
                Observer {
                    setPageMode()
                    setupSeekBar()
                    setupViewPager()
                    setupPagination()
                })
        }
    }

    private fun EPubReaderViewModel.calcPageIfNotCached() {
        Handler().postDelayed({
            if (!cached(requireContext())) {
                calcPageCount(requireActivity())
            }
        }, 100)
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
        bottom_nv_seek_bar.apply {
            progress = 1 // based on 1 page
            max = viewModel.getPageCount().value!!
        }

        updateCurrentPageInfo()

        viewModel.currentPageIdx.observe(requireActivity(),
            Observer { idx ->
                if (lockedPaging(idx)) return@Observer

                val curPage = idx
                updatePageInfo(curPage)

                if(SettingsPreference.getViewMode(context)) {
                    view_pager.setCurrentItem(curPage, false)
                } else {
                    bottom_nv_seek_bar.progress = curPage
                }
            })

        RxSeekBar.changeEvents(bottom_nv_seek_bar)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { seekBarChangeEvent ->
                when (seekBarChangeEvent) {
                    is SeekBarStopChangeEvent -> {
                        val curPage = bottom_nv_seek_bar.progress
                        updatePageInfo(curPage)
                        if(SettingsPreference.getViewMode(context)) {
                            view_pager.setCurrentItem(curPage, false)
                        } else {
                            val webView = getCurrentWebView()
                            webView?.let { viewModel.loadPageByIndex(requireContext(), it, curPage) }
                        }
                    }
                }
            }

        view_pager.pageSelections()
            .subscribeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe { e ->
                val curPage = e
                bottom_nv_seek_bar.progress = curPage
                updatePageInfo(curPage)
            }
    }

    private fun getCurrentWebView(): ReaderWebView? {
        return (view_pager[0] as RecyclerView).findViewHolderForAdapterPosition(view_pager.currentItem)?.itemView?.web_view
    }

    private fun lockedPaging(idx: Int?) = idx != 0 && viewModel.pageLock

    private fun updatePageInfo(curPage: Int) {
        tv_page_info.text = "${curPage + 1}/${bottom_nv_seek_bar.max}"
    }

    private fun updateCurrentPageInfo() {
        view_pager.setCurrentItem(bottom_nv_seek_bar.progress!!, false)
    }

    private fun setupViewPager() {
        view_pager.apply {
            offscreenPageLimit = 3
            val pageAdapter = PageAdapter(requireContext(), viewModel)
            pageAdapter.setHasStableIds(true)
            adapter = pageAdapter
        }
    }

    private fun setupPagination() {
        viewModel.ePub.apply {
            pagination(
                viewModel.ePub.getSpineList(),
                viewModel.pageCountByRendering.value!!,
                viewModel.pages4ChapterByRendering
            )
        }
    }

    fun loadSpecificSpine(idx: Int) {
        val webView = getCurrentWebView()
        if (webView != null) {
            viewModel.loadSpineByIndex(requireContext(), webView, idx)
        }
    }

    fun loadSpecificSpine(path: String) {
        val webView = getCurrentWebView()
        if (webView != null) {
            viewModel.loadChapterByAbsolutePath(requireContext(), webView, path)
        }
    }

    fun reloadCurrentPage() {
        setPageMode()
        if (!viewModel.cached(requireContext())) {
            load()
        }
    }

    fun applyAnimation() {
        setAnimationMode()
    }

    private fun setPageMode() {
        val pageMode = SettingsPreference.getViewMode(context)
        view_pager.isUserInputEnabled = pageMode
        view_pager.adapter?.notifyDataSetChanged()
    }

    private fun setAnimationMode() {
        val effectNumber = SettingsPreference.getAnimationMode(requireContext())
        val effect = viewModel.getEffect(effectNumber)
        val transformer = ViewPager2.PageTransformer(effect)
        view_pager.setPageTransformer(transformer)
    }

    companion object {
        fun newInstance() = EPubReaderFragment()
    }
}