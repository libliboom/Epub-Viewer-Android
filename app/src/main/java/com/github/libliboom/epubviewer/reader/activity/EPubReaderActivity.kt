package com.github.libliboom.epubviewer.reader.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.reader.fragment.EPubReaderFragment
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel

/**
 * Option for reading book vertically
 * - scroll event
 *      [x] go to next, previous page
 *      [x] load new page from top
 *      [ ] progress bar
 *      [ ] cache
 *      [ ] effect
 *      [ ] how to handle javascript event
 *      [ ] page movement
 *      [ ] find way for internal link
 */
class EPubReaderActivity : ReaderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epub_reader)

        updateViewModel()

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_fragment, EPubReaderFragment.newInstance())
            .commit()
    }

    private fun updateViewModel() {
        val viewModel = ViewModelProvider(this, factory).get(EPubReaderViewModel::class.java)
        viewModel.ePubFile = intent.getStringExtra(EXTRA_EPUB_FILE)
    }

    companion object {
        private const val EXTRA_EPUB_FILE = "extra_epub_file"

        fun newIntent(context: Context, ePubFile: String): Intent {
            val intent = Intent(context, EPubReaderActivity::class.java)
            intent.putExtra(EXTRA_EPUB_FILE, ePubFile)
            return intent
        }
    }
}
