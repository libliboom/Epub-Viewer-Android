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
import com.github.libliboom.epubviewer.databinding.FragmentEpubReaderBinding
import com.github.libliboom.epubviewer.db.preference.SettingsPreference
import com.github.libliboom.epubviewer.reader.view.ReaderWebView
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.epubviewer.reader.viewpager.adapter.PageAdapter
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.jakewharton.rxbinding2.widget.SeekBarStopChangeEvent
import com.jakewharton.rxbinding4.viewpager2.pageSelections
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.item_web_view.view.web_view

class EPubReaderFragment : BaseFragment() {

    private val viewModel: EPubReaderViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(EPubReaderViewModel::class.java)
    }

    private val binding: FragmentEpubReaderBinding by lazy {
        getBinding() as FragmentEpubReaderBinding
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
            pageCountByRendering.observe(
                requireActivity(),
                Observer {
                    setPageMode()
                    setupSeekBar()
                    setupViewPager()
                    setupPagination()
                }
            )
        }
    }

    private fun EPubReaderViewModel.calcPageIfNotCached() {
        Handler().postDelayed(
            {
                if (!cached(requireContext())) {
                    calcPageCount(requireActivity())
                }
            },
            100
        )
    }

    private fun setupBottomNavigation() {
        binding.readerBottomNv.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_contents -> viewModel.startContentsActivity(requireActivity())
                R.id.nav_settings -> viewModel.startSettingActivity(requireActivity())
                else -> Toast.makeText(requireActivity(), "Do nothing", Toast.LENGTH_SHORT).show()
            }

            true
        }
    }

    private fun setupSeekBar() {
        binding.readerBottomNvSeekBar.apply {
            progress = 1 // based on 1 page
            max = viewModel.getPageCount().value!!
        }

        updateCurrentPageInfo()

        viewModel.currentPageIdx.observe(
            requireActivity(),
            Observer { idx ->
                if (lockedPaging(idx)) return@Observer

                val curPage = idx
                updatePageInfo(curPage)

                if (SettingsPreference.getViewMode(context)) {
                    binding.readerViewPager.setCurrentItem(curPage, false)
                } else {
                    binding.readerBottomNvSeekBar.progress = curPage
                }
            }
        )

        RxSeekBar.changeEvents(binding.readerBottomNvSeekBar)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { seekBarChangeEvent ->
                when (seekBarChangeEvent) {
                    is SeekBarStopChangeEvent -> {
                        val curPage = binding.readerBottomNvSeekBar.progress
                        updatePageInfo(curPage)
                        if (SettingsPreference.getViewMode(context)) {
                            binding.readerViewPager.setCurrentItem(curPage, false)
                        } else {
                            val webView = getCurrentWebView()
                            webView?.let { viewModel.loadPageByIndex(requireContext(), it, curPage) }
                        }
                    }
                }
            }

        binding.readerViewPager.pageSelections()
            .subscribeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe { e ->
                val curPage = e
                binding.readerBottomNvSeekBar.progress = curPage
                updatePageInfo(curPage)
            }
    }

    private fun getCurrentWebView(): ReaderWebView? {
        return (binding.readerViewPager[0] as RecyclerView).findViewHolderForAdapterPosition(
            binding.readerViewPager.currentItem
        )?.itemView?.web_view
    }

    private fun lockedPaging(idx: Int?) = idx != 0 && viewModel.pageLock

    private fun updatePageInfo(curPage: Int) {
        binding.readerTvPageInfo.text = "${curPage + 1}/${binding.readerBottomNvSeekBar.max}"
    }

    private fun updateCurrentPageInfo() {
        binding.readerViewPager.setCurrentItem(binding.readerBottomNvSeekBar.progress!!, false)
    }

    private fun setupViewPager() {
        binding.readerViewPager.apply {
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
        binding.readerViewPager.apply {
            val pageMode = SettingsPreference.getViewMode(context)
            isUserInputEnabled = pageMode
            adapter?.notifyDataSetChanged()
        }
    }

    private fun setAnimationMode() {
        val effectNumber = SettingsPreference.getAnimationMode(requireContext())
        val effect = viewModel.getEffect(effectNumber)
        val transformer = ViewPager2.PageTransformer(effect)
        binding.readerViewPager.setPageTransformer(transformer)
    }

    companion object {
        fun newInstance() = EPubReaderFragment()
    }
}
