package com.github.libliboom.epubviewer.main.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.github.libliboom.epubviewer.R
import kotlinx.android.synthetic.main.activity_epub_reader.bottom_nv_reader

class EpubReaderActivity : ViewerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epub_reader)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottom_nv_reader.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_contents -> startActivity(
                    Intent(
                        this@EpubReaderActivity,
                        ContentsActivity::class.java
                    )
                )
                R.id.nav_settings -> startActivity(
                    Intent(
                        this@EpubReaderActivity,
                        SettingsActivity::class.java
                    )
                )
                else -> Toast.makeText(applicationContext, "Do nothing", Toast.LENGTH_SHORT).show()
            }

            true
        }
    }

    override fun getTitleToolbar(): String {
        return "Title of book"
    }
}
