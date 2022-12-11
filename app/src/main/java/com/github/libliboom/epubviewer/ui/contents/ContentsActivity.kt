package com.github.libliboom.epubviewer.ui.contents

import android.content.Context
import android.content.Intent
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.databinding.ActivityContentsBinding
import com.github.libliboom.epubviewer.presentation.contents.ContentsStore.State
import com.github.libliboom.epubviewer.ui.viewer.ReaderActivity
import com.github.libliboom.epubviewer.util.resource.StringUtils

interface ContentsViewBinder {
  fun represent(state: State)
}

data class ContentsParameter(
  val cover: String,
  val chapters: List<String>,
  val srcs: List<String>
)

class ContentsActivity : ReaderActivity<ActivityContentsBinding>() {

  private lateinit var parameter: ContentsParameter

  override fun inflateBinding() = ActivityContentsBinding.inflate(layoutInflater)

  override fun getArgs() {
    intent ?: return
    val cover = intent.getStringExtra(EXTRA_COVER) ?: EXTRA_TITLE_DEFAULT
    val chapters = intent.getStringArrayListExtra(EXTRA_CHAPTERS) ?: emptyList()
    val srcs = intent.getStringArrayListExtra(EXTRA_SRCS) ?: emptyList()
    parameter = ContentsParameter(cover, chapters, srcs)
  }

  override fun initView(binding: ActivityContentsBinding) {
    if (::parameter.isInitialized.not()) finish()
    super.initView(binding)
    updateTitle(StringUtils.getString(R.string.er_toolbar_title_contents))
    supportFragmentManager.beginTransaction()
      .add(
        R.id.contents_container,
        ContentsFragment.newInstance(parameter)
      ).commit()
  }

  companion object {
    private const val EXTRA_COVER = "extra_cover"
    private const val EXTRA_CHAPTERS = "extra_chapters"
    private const val EXTRA_SRCS = "extra_srcs"
    private const val EXTRA_TITLE_DEFAULT = "No title"

    fun newIntent(context: Context, parameter: ContentsParameter): Intent {
      val intent = Intent(context, ContentsActivity::class.java)
      intent.putExtra(EXTRA_COVER, parameter.cover)
      intent.putStringArrayListExtra(EXTRA_CHAPTERS, parameter.chapters as ArrayList<String>?)
      intent.putStringArrayListExtra(EXTRA_SRCS, parameter.srcs as ArrayList<String>?)
      return intent
    }
  }
}
