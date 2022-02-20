package com.github.libliboom.epubviewer.ui.viewer

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.app.ui.BaseActivity
import com.github.libliboom.epubviewer.databinding.ActivityEpubReaderBinding
import com.github.libliboom.epubviewer.ui.contents.ContentsFragment.Companion.EXTRA_INDEX_OF_CHAPTER
import com.github.libliboom.epubviewer.ui.settings.SettingsFragment.Companion.EXTRA_SETTINGS_ANIMATION_MODE
import com.github.libliboom.epubviewer.ui.settings.SettingsFragment.Companion.EXTRA_SETTINGS_VIEW_MODE
import com.github.libliboom.epubviewer.ui.viewer.EPubReaderViewModel.Companion.REQUEST_CODE_CHAPTER
import com.github.libliboom.epubviewer.ui.viewer.EPubReaderViewModel.Companion.REQUEST_CODE_VIEW_MODE

class EPubReaderActivity : BaseActivity<ActivityEpubReaderBinding>() {

  private val viewModel: EPubReaderViewModel by viewModels()

  private val ePubReaderFragment by lazy { EPubReaderFragment.newInstance() }

  override fun inflateBinding() = ActivityEpubReaderBinding.inflate(layoutInflater)

  override fun initView(binding: ActivityEpubReaderBinding) {
    super.initView(binding)
    updateViewModel()

    supportFragmentManager.beginTransaction()
      .add(R.id.reader_frame_layout, ePubReaderFragment)
      .commit()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode != Activity.RESULT_OK) return

    when (requestCode) {
      REQUEST_CODE_CHAPTER -> {
        val path = data?.getStringExtra(EXTRA_INDEX_OF_CHAPTER)
        path?.let { ePubReaderFragment.loadSpecificSpine(path) }
      }
      REQUEST_CODE_VIEW_MODE -> {
        val changed = data?.getBooleanExtra(EXTRA_SETTINGS_VIEW_MODE, false)
        if (changed == true) ePubReaderFragment.reloadCurrentPage()
        val effected = data?.getBooleanExtra(EXTRA_SETTINGS_ANIMATION_MODE, false)
        if (effected == true) ePubReaderFragment.applyAnimation()
      }
    }
  }

  private fun updateViewModel() {
    viewModel.ePubFilePath = intent.getStringExtra(EXTRA_EPUB_FILE)
  }

  companion object {
    private const val EXTRA_EPUB_FILE = "extra_epub_file"

    fun newIntent(context: Context, ePubFile: String): Intent {
      val intent = Intent(context, EPubReaderActivity::class.java)
      intent.putExtra(EXTRA_EPUB_FILE, ePubFile)
      return intent
    }
  }
}
