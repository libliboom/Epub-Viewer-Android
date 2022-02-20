package com.github.libliboom.epubviewer.ui.splash

import androidx.lifecycle.lifecycleScope
import com.github.libliboom.epubviewer.BuildConfig
import com.github.libliboom.epubviewer.app.ui.BaseActivity
import com.github.libliboom.epubviewer.databinding.ActivitySplashBinding
import com.github.libliboom.epubviewer.ui.bookshelf.BookshelfActivity
import com.github.libliboom.epubviewer.util.ui.AppBarUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

  override fun inflateBinding() = ActivitySplashBinding.inflate(layoutInflater)

  override fun initView(binding: ActivitySplashBinding) {
    super.initView(binding)
    supportActionBar?.let { AppBarUtils.setFullScreen(it) }
    lifecycleScope.launch {
      delay(if (BuildConfig.DEBUG) 0 else 2000)
      startActivity(BookshelfActivity.newIntent(applicationContext))
      finish()
    }
  }
}
