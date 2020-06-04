package com.github.libliboom.epubviewer.util.file

import android.content.Context
import com.github.libliboom.epubviewer.dev.EPubFileStub
import java.io.File

/**
 * xxxPath must return the path with '/'
 */
object StorageManager {

    fun getBooksPath(context: Context): String {
        return getRootDirPath(context) + EPubFileStub.BOOKS_PATH
    }

    fun getExtractedPath(context: Context): String {
        return getRootDirPath(context) + EPubFileStub.EXTRACTED_EPUB_FILE_PATH
    }

    fun getCachedPath(context: Context): String {
        return getRootDirPath(context) + EPubFileStub.EXTRACTED_EPUB_FILE_PATH
    }

    private fun getRootDirPath(context: Context): String {
        val dir = context.externalCacheDir?.path
        return takeIf { dir != null }?.let { dir + File.separator } ?: ""
    }
}
