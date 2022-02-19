package com.github.libliboom.epubviewer.main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.base.BaseActivity
import com.github.libliboom.epubviewer.main.fragment.ContentsFragment
import java.util.ArrayList

class ContentsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents)
        //updateTitle(getString(R.string.er_toolbar_title_contents))

        val cover = intent.getStringExtra(EXTRA_COVER) ?: EXTRA_TITLE_DEFAULT
        val chapters = intent.getStringArrayListExtra(EXTRA_CHAPTERS)
        val srcs = intent.getStringArrayListExtra(EXTRA_SRCS)

        supportFragmentManager.beginTransaction()
            .add(R.id.reader_frame_layout, ContentsFragment.newInstance(cover, chapters, srcs))
            .commit()
    }

    companion object {
        private const val EXTRA_COVER = "extra_cover"
        private const val EXTRA_CHAPTERS = "extra_chapters"
        private const val EXTRA_SRCS = "extra_srcs"
        private const val EXTRA_TITLE_DEFAULT = "No title"

        fun newIntent(
            context: Context,
            cover: String,
            chapters: List<String>,
            srcs: List<String>
        ): Intent {
            val intent = Intent(context, ContentsActivity::class.java)
            intent.putExtra(EXTRA_COVER, cover)
            intent.putStringArrayListExtra(EXTRA_CHAPTERS, chapters as ArrayList<String>?)
            intent.putStringArrayListExtra(EXTRA_SRCS, srcs as ArrayList<String>?)
            return intent
        }
    }
}
