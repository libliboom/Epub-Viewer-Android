package com.github.libliboom.epubviewer.main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.fragment.ContentsFragment
import com.github.libliboom.epubviewer.reader.activity.ReaderActivity

class ContentsActivity : ReaderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents)
        updateTitle(getString(R.string.er_toolbar_title_contents))

        val href = intent.getStringExtra(EXTRA_TITLE) ?: EXTRA_TITLE_DEFAULT
        val chapters = intent.getStringArrayListExtra(EXTRA_CHAPTERS)

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_fragment, ContentsFragment.newInstance(href, chapters))
            .commit()
    }

    companion object {
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_CHAPTERS = "extra_chapters"
        private const val EXTRA_TITLE_DEFAULT = "No title"

        fun newIntent(context: Context, title: String, list: ArrayList<String>): Intent {
            val intent = Intent(context, ContentsActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putStringArrayListExtra(EXTRA_CHAPTERS, list)
            return intent
        }
    }
}
