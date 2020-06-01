package com.github.libliboom.epubviewer.reader.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseFragment
import com.github.libliboom.epubviewer.db.preference.SettingsPreference
import com.github.libliboom.epubviewer.reader.viewpager.adapter.PageAdapter
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.jakewharton.rxbinding2.widget.SeekBarStopChangeEvent
import com.jakewharton.rxbinding4.viewpager2.pageSelections
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_epub_reader.bottom_nv_reader
import kotlinx.android.synthetic.main.fragment_epub_reader.bottom_nv_seek_bar
import kotlinx.android.synthetic.main.fragment_epub_reader.tv_page_info
import kotlinx.android.synthetic.main.fragment_epub_reader.view_pager
import kotlinx.android.synthetic.main.item_web_view.web_view

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
            initEpub(requireContext())
            calcPageCount(requireActivity())
            pageCountByRendering.observe(requireActivity(),
                Observer {
                    setPageMode()
                    setupSeekBar()
                    setupViewPager()
                    setupPagination()
                })
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
                            viewModel.loadPageByIndex(requireContext(), web_view, curPage)
                        }
                    }
                }
            }

        view_pager.pageSelections()
            .subscribeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe {e ->
                val curPage = e
                bottom_nv_seek_bar.progress = curPage
                updatePageInfo(curPage)
            }
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
            //adapter = PageAdapter(requireContext(), viewModel)
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
        viewModel.loadSpineByIndex(requireContext(), web_view, idx)
    }

    fun loadSpecificSpine(path: String) {
        viewModel.loadChapterByAbsolutePath(requireContext(), web_view, path)
    }

    // FIXME: 2020/05/31 
    fun reloadCurrentPage() {
        //load()
        //setPageMode()
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