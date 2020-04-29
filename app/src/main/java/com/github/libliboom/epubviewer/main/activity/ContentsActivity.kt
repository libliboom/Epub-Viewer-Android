package com.github.libliboom.epubviewer.main.activity

import android.os.Bundle
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.fragment.ContentsFragment

class ContentsActivity : ViewerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents)

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_fragment, ContentsFragment.newInstance())
            .commit()
    }

    override fun getTitleToolbar(): String {
        return "Contents"
    }
}
