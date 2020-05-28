package com.github.libliboom.epubviewer.reader.fragment

import android.annotation.SuppressLint
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

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()

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

    @SuppressLint("CheckResult")
    private fun setupSeekBar() {
        bottom_nv_seek_bar.apply {
            progress = 1 // based on 1 page
            max = viewModel.getPageCount().value!!
        }

        updateCurrentPageInfo()

        viewModel.currentPageIdx.observe(requireActivity(),
            Observer { idx ->
                bottom_nv_seek_bar.progress = idx
                updateCurrentPageInfo()
                bottom_nv_seek_bar.invalidate()
            })

        RxSeekBar.changeEvents(bottom_nv_seek_bar)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { seekBarChangeEvent ->
                when (seekBarChangeEvent) {
                    is SeekBarStopChangeEvent -> {
                        updateCurrentPageInfo()
                        viewModel.updateChapterIndex(bottom_nv_seek_bar.progress)
                    }
                }
            }

        view_pager.pageSelections()
            .subscribeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe {e ->
                bottom_nv_seek_bar.progress = e + 1 // based on 1 page
                tv_page_info.text = "${bottom_nv_seek_bar.progress}/${bottom_nv_seek_bar.max}"
                viewModel.updateChapterIndex(bottom_nv_seek_bar.progress)
            }
    }

    private fun updateCurrentPageInfo() {
        view_pager.setCurrentItem(bottom_nv_seek_bar.progress!!, false)
    }

    private fun setupViewPager() {
        view_pager.apply {
            offscreenPageLimit = 1
            adapter = PageAdapter(requireContext(), viewModel)
        }
    }

    private fun setupPagination() {
        viewModel.ePub.apply {
            pagination(
                viewModel.ePub.getFileList(),
                viewModel.pageCountByRendering.value!!,
                viewModel.pages4ChapterByRendering
            )
        }
    }

    fun loadSpecificChapter(idx: Int) {
        viewModel.loadChapterByChapterIndex(requireContext(), web_view, idx)
    }

    fun reloadCurrentPage() {
        setPageMode()
        setAnimationMode()
        viewModel.loadPageByPageIndex(requireContext(), web_view, bottom_nv_seek_bar.progress)
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