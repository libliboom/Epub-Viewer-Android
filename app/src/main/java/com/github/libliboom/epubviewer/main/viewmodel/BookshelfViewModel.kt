package com.github.libliboom.epubviewer.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.libliboom.epubviewer.dev.EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_01
import com.github.libliboom.epubviewer.dev.EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_02
import com.github.libliboom.epubviewer.dev.EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_03
import com.github.libliboom.epubviewer.dev.EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_04
import com.github.libliboom.epubviewer.dev.EPubFileStub.ASSET_EXTRACTED_COVER_FILE_PATH_05
import com.github.libliboom.epubviewer.dev.EPubFileStub.BOOKS_PATH
import com.github.libliboom.epubviewer.util.file.StorageManager
import com.github.libliboom.utils.io.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

// TODO: 2020/05/13 Fetch data form local database
class BookshelfViewModel : ViewModel {

    var ePubFiles = listOf<String>()

    val coverPaths = listOf(
        ASSET_EXTRACTED_COVER_FILE_PATH_01,
        ASSET_EXTRACTED_COVER_FILE_PATH_02,
        ASSET_EXTRACTED_COVER_FILE_PATH_03,
        ASSET_EXTRACTED_COVER_FILE_PATH_04,
        ASSET_EXTRACTED_COVER_FILE_PATH_05
    )

    @Inject
    constructor()

    fun initResources(context: Context) {
        ePubFiles = context.assets.list("books/")?.toList() as List<String>
        forceInitialization(context)
    }

    private fun forceInitialization(context: Context) {
        val cachedBooksPath = createBooksDirIfNotExist(context)

        lateinit var istream: InputStream
        lateinit var ostream: OutputStream

        try {
            for (f in ePubFiles) {
                val assetsFile = BOOKS_PATH + f
                istream = context.assets.open(assetsFile)
                ostream = FileOutputStream(File(cachedBooksPath + f))
                FileUtils.copy(istream, ostream)
            }
        } finally {
            istream.close()
            ostream.close()
        }
    }

    private fun createBooksDirIfNotExist(context: Context): String {
        val cachedBooksPath = StorageManager.getBooksPath(context)
        val dir = File(cachedBooksPath)
        if (!dir.exists()) dir.mkdirs()
        return cachedBooksPath
    }
}