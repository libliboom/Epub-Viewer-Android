package com.github.libliboom.epubviewer.main.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.fragment.BookshelfFragment

class BookshelfActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookshelf)

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_fragment, BookshelfFragment.newInstance())
            .commit()
    }
}