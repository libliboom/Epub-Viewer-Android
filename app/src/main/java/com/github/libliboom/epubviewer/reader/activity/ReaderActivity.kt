package com.github.libliboom.epubviewer.reader.activity

import android.os.Bundle
import com.github.libliboom.epubviewer.base.BaseActivity

abstract class ReaderActivity : BaseActivity() {

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
