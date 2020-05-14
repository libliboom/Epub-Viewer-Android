package com.github.libliboom.epubviewer.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.libliboom.epubviewer.dev.EPubFileStub
import com.github.libliboom.epubviewer.dev.EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_01
import com.github.libliboom.epubviewer.dev.EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_02
import com.github.libliboom.epubviewer.dev.EPubFileStub.EPUB_FILE_PATH_01
import com.github.libliboom.epubviewer.dev.EPubFileStub.EPUB_FILE_PATH_02
import com.github.libliboom.epubviewer.util.file.StorageManager
import com.github.libliboom.utils.io.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

// TODO: 2020/05/13 Fetch data form local database
class BookshelfViewModel : ViewModel {
    val ePubFiles = listOf(
        EPUB_FILE_PATH_01,
        EPUB_FILE_PATH_02
    )

    val coverPaths = listOf(
        ASSET_EXTRACTED_COVER_FILE_PATH_01,
        ASSET_EXTRACTED_COVER_FILE_PATH_02
    )

    @Inject
    constructor()

    fun forceInitialization(context: Context) {
        val cached = StorageManager.getDir(context)
        val dir = File(cached + EPubFileStub.BOOKS_PATH)
        if (!dir.exists()) dir.mkdirs()

        lateinit var istream: InputStream
        lateinit var ostream: OutputStream

        try {
            for (f in ePubFiles) {
                istream = context.assets.open(f)
                ostream = FileOutputStream(File(cached + f))
                FileUtils.copy(istream, ostream)
            }
        } finally {
            istream.close()
            ostream.close()
        }
    }
}