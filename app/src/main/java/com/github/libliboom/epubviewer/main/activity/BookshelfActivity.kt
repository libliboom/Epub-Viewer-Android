package com.github.libliboom.epubviewer.main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseActivity
import com.github.libliboom.epubviewer.main.fragment.BookshelfFragment
import com.github.libliboom.epubviewer.main.viewmodel.BookshelfViewModel

class BookshelfActivity : BaseActivity() {

    private val viewModel: BookshelfViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookshelf)

        viewModel.initResources(applicationContext)

        supportFragmentManager.beginTransaction()
            .add(R.id.reader_frame_layout, BookshelfFragment.newInstance())
            .commit()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BookshelfActivity::class.java)
        }
    }
}
