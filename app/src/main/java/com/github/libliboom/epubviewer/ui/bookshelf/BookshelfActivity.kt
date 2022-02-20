package com.github.libliboom.epubviewer.ui.bookshelf

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.app.ui.BaseActivity
import com.github.libliboom.epubviewer.databinding.ActivityBookshelfBinding
import com.github.libliboom.epubviewer.presentation.bookshelf.BookshelfStore.State

interface BookshelfViewBinder {
  fun represent(state: State)
}

class BookshelfActivity : BaseActivity<ActivityBookshelfBinding>() {

  private val viewModel: BookshelfViewModel by viewModels()

  override fun inflateBinding() = ActivityBookshelfBinding.inflate(layoutInflater)

  override fun initView(binding: ActivityBookshelfBinding) {
    super.initView(binding)
    viewModel.initResources(applicationContext)
    supportFragmentManager.beginTransaction()
      .add(R.id.reader_frame_layout, BookshelfFragment.newInstance())
      .commit()
  }

  companion object {
    fun newIntent(context: Context) = Intent(context, BookshelfActivity::class.java)
  }
}
