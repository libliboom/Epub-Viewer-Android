package com.github.libliboom.epubviewer.reader.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.main.fragment.ContentsFragment.Companion.EXTRA_INDEX_OF_CHAPTER
import com.github.libliboom.epubviewer.main.fragment.SettingsFragment.Companion.EXTRA_SETTINGS_ANIMATION_MODE
import com.github.libliboom.epubviewer.main.fragment.SettingsFragment.Companion.EXTRA_SETTINGS_VIEW_MODE
import com.github.libliboom.epubviewer.reader.fragment.EPubReaderFragment
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel.Companion.REQUEST_CODE_CHAPTER
import com.github.libliboom.epubviewer.reader.viewmodel.EPubReaderViewModel.Companion.REQUEST_CODE_VIEW_MODE
import javax.inject.Inject

/**
 * Option for reading book vertically
 * - scroll event
 *      [x] go to next, previous page
 *      [x] load new page from top
 *      [ ] progress bar
 *      [ ] cache
 *      [ ] effect
 *      [x] how to handle javascript event
 *      [x] page movement
 *      [ ] find way for internal link
 */
class EPubReaderActivity : ReaderActivity() {

    @Inject
    lateinit var ePubReaderFragment: EPubReaderFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epub_reader)

        updateViewModel()

        supportFragmentManager.beginTransaction()
            .add(R.id.reader_frame_layout, ePubReaderFragment)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE_CHAPTER -> {
                val path = data?.getStringExtra(EXTRA_INDEX_OF_CHAPTER)
                path?.let { ePubReaderFragment.loadSpecificSpine(path) }
            }
            REQUEST_CODE_VIEW_MODE -> {
                val changed = data?.getBooleanExtra(EXTRA_SETTINGS_VIEW_MODE,false)
                if (changed == true) ePubReaderFragment.reloadCurrentPage()
                val effected = data?.getBooleanExtra(EXTRA_SETTINGS_ANIMATION_MODE,false)
                if (effected == true) ePubReaderFragment.applyAnimation()
            }
        }
    }

    private fun updateViewModel() {
        val viewModel = ViewModelProvider(this, factory).get(EPubReaderViewModel::class.java)
        viewModel.ePubFilePath = intent.getStringExtra(EXTRA_EPUB_FILE)
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
