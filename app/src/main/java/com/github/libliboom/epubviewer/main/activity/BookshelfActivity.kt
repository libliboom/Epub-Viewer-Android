package com.github.libliboom.epubviewer.main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseActivity
import com.github.libliboom.epubviewer.main.fragment.BookshelfFragment
import com.github.libliboom.epubviewer.main.viewmodel.BookshelfViewModel

class BookshelfActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookshelf)

        val viewModel = ViewModelProvider(this, factory).get(BookshelfViewModel::class.java)
        viewModel.forceInitialization(applicationContext)

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_fragment, BookshelfFragment.newInstance())
            .commit()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BookshelfActivity::class.java)
        }
    }
}