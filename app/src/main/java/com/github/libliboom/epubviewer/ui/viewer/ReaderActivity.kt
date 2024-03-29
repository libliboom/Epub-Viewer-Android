package com.github.libliboom.epubviewer.ui.viewer

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.github.libliboom.epubviewer.app.ui.BaseActivity

abstract class ReaderActivity<Binding : ViewBinding> : BaseActivity<Binding>() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setupActionbar()
  }

  private fun setupActionbar() {
    supportActionBar?.apply { setDisplayHomeAsUpEnabled(true) }
  }

  protected open fun updateTitle(title: String) {
    supportActionBar?.apply { this.title = title }
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }
}
