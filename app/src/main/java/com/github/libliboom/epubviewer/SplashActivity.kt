package com.github.libliboom.epubviewer

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.github.libliboom.epubviewer.main.activity.BookshelfActivity
import com.github.libliboom.epubviewer.util.ui.AppBarUtils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.let { AppBarUtils.setFullScreen(it) }

        val delayMillis: Long = if (BuildConfig.DEBUG) 0 else 2000

        Handler().postDelayed({
            startActivity(BookshelfActivity.newIntent(applicationContext))
            finish()
        }, delayMillis)
    }
}
