package com.github.libliboom.epubviewer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.github.libliboom.epubviewer.main.activity.BookshelfActivity
import com.github.libliboom.epubviewer.utils.ui.AppBarUtils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        AppBarUtils.setFullScreen(supportActionBar)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, BookshelfActivity::class.java))
            finish()
        }, 2000)
    }
}
