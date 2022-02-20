package com.github.libliboom.epubviewer.ui.viewer

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.app.ui.BaseFragment
import com.github.libliboom.epubviewer.databinding.FragmentEpubReaderBinding
import com.github.libliboom.epubviewer.db.preference.SettingsPreference
import com.github.libliboom.epubviewer.db.room.BookRoomDatabase
import com.github.libliboom.epubviewer.ui.contents.ContentsActivity
import com.github.libliboom.epubviewer.ui.contents.ContentsParameter
import com.github.libliboom.epubviewer.ui.settings.SettingsActivity
import com.github.libliboom.epubviewer.ui.viewer.EPubReaderViewModel.Companion.REQUEST_CODE_CHAPTER
import com.github.libliboom.epubviewer.ui.viewer.viewpager.adapter.PageAdapter
import com.github.libliboom.epubviewer.util.file.EPubUtils
import com.github.libliboom.epubviewer.util.file.StorageManager
import com.github.libliboom.epubviewer.util.ui.TranslationUtils
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.jakewharton.rxbinding2.widget.SeekBarStopChangeEvent
import com.jakewharton.rxbinding4.viewpager2.pageSelections
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_web_view.view.web_view

class EPubReaderFragment : BaseFragment<FragmentEpubReaderBinding>() {

  private val viewModel: EPubReaderViewModel by activityViewModels()

  private lateinit var disposableSeekBar: Disposable

  override fun inflateBinding(container: ViewGroup?) =
    FragmentEpubReaderBinding.inflate(layoutInflater, container, false)

  override fun afterInitView(binding: FragmentEpubReaderBinding) {
    super.afterInitView(binding)
    setupBottomNavigation()
    load()
  }

  override fun onDestroyView() {
    if (disposableSeekBar.isDisposed.not()) {
      disposableSeekBar.dispose()
    }

    super.onDestroyView()
  }

  private fun load() {
    viewModel.run {
      val bookDao = BookRoomDatabase.getDatabase(requireContext(), viewModelScope).bookDao()
      initDatabase(bookDao, requireActivity())
      val booksPath = StorageManager.getBooksPath(requireContext())
      val extractedPath = StorageManager.getExtractedPath(requireContext())
      initEpub(booksPath, extractedPath)
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
        if (cached(SettingsPreference.getViewMode(context)).not()) {
          calcPageCount(requireActivity())
        }
      },
      100
    )
  }

  private fun setupBottomNavigation() {
    binding.readerBottomNv.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.nav_contents -> startContentsActivity()
        R.id.nav_settings -> startSettingActivity()
        else -> Toast.makeText(requireActivity(), "Do nothing", Toast.LENGTH_SHORT).show()
      }

      true
    }
  }

  private fun startContentsActivity() {
    viewModel.apply {
      val cover = EPubUtils.getCover(ePub)
      val chapters = fetchChapters(EPubUtils.getNcx(ePub))
      val srcs = fetchSrc(EPubUtils.getNcx(ePub))
      requireActivity().startActivityForResult(
        ContentsActivity.newIntent(
          requireActivity(),
          ContentsParameter(cover, chapters, srcs)
        ),
        REQUEST_CODE_CHAPTER
      )
    }
  }

  private fun startSettingActivity() {
    requireActivity().startActivityForResult(
      SettingsActivity.newIntent(requireActivity()), EPubReaderViewModel.REQUEST_CODE_VIEW_MODE
    )
  }

  private fun setupSeekBar() {
    binding.readerBottomNvSeekBar.apply {
      progress = 1 // based on 1 page
      max = viewModel.getPageCount().value!!
    }

    updateCurrentPageInfo()

    viewModel.currentPageIdx.observe(
      requireActivity(),
      Observer { curPage ->
        if (lockedPaging(curPage)) return@Observer

        updatePageInfo(curPage)

        if (SettingsPreference.getViewMode(context)) {
          binding.readerViewPager.setCurrentItem(curPage, false)
        } else {
          binding.readerBottomNvSeekBar.progress = curPage
        }
      }
    )

    disposableSeekBar = RxSeekBar.changeEvents(binding.readerBottomNvSeekBar)
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
              val decompressedPath = StorageManager.getExtractedPath(requireContext())
              webView?.run {
                val pageInfo = viewModel.loadPageByIndex(decompressedPath, curPage)
                loadUrl(EPubUtils.getUri(pageInfo))
              }
            }
          }
        }
      }

    binding.readerViewPager.pageSelections()
      .subscribeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
      .subscribe { curPage ->
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
    val pageInfo = "${curPage + 1}/${binding.readerBottomNvSeekBar.max}"
    binding.readerTvPageInfo.text = pageInfo
  }

  private fun updateCurrentPageInfo() {
    binding.readerViewPager.setCurrentItem(binding.readerBottomNvSeekBar.progress, false)
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
      val pageInfo = viewModel.loadSpineByIndex(
        StorageManager.getExtractedPath(requireContext()),
        idx
      )
      webView.loadUrl(EPubUtils.getUri(pageInfo))
    }
  }

  fun loadSpecificSpine(path: String) {
    val webView = getCurrentWebView()
    if (webView != null) {
      val decompressedPath = StorageManager.getExtractedPath(requireContext())
      val uri = viewModel.loadChapterByAbsolutePath(decompressedPath, path)
      webView.loadUrl(uri)
    }
  }

  fun reloadCurrentPage() {
    setPageMode()
    if (viewModel.cached(SettingsPreference.getViewMode(context)).not()) {
      load()
    }
  }

  fun applyAnimation() {
    setAnimationMode()
  }

  private fun setPageMode() {
    binding.readerViewPager.apply {
      val pageMode = SettingsPreference.getViewMode(context)
      if (pageMode.not()) viewModel.pageLock = true
      isUserInputEnabled = pageMode
      adapter?.notifyDataSetChanged()
    }
  }

  private fun setAnimationMode() {
    val effectNumber = SettingsPreference.getAnimationMode(requireContext())
    val effect = getEffect(effectNumber)
    val transformer = ViewPager2.PageTransformer(effect)
    binding.readerViewPager.setPageTransformer(transformer)
  }

  private fun getEffect(n: Int): (page: View, position: Float) -> Unit {
    return TranslationUtils.run {
      when (n) {
        EFFECT_NONE -> effectNone()
        EFFECT_CUBE_OUT_DEPTH -> effectCubeOutDepth()
        EFFECT_ZOOM_OUT_PAGE -> effectZoomOutPageEffect()
        EFFECT_GEO -> effectGeo()
        EFFECT_FADE_OUT -> effectFadeOut()
        else -> effectNone()
      }
    }
  }

  companion object {
    fun newInstance() = EPubReaderFragment()
  }
}
