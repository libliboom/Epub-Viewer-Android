package com.github.libliboom.epubviewer.reader.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.libliboom.epub.EPub
import com.github.libliboom.epub.outline.opf.NavigationControlXml
import com.github.libliboom.epubviewer.dev.EPubFileStub.EXTRACTED_EPUB_FILE_PATH
import com.github.libliboom.epubviewer.dev.EPubFileStub.OEBPS_PATH
import com.github.libliboom.epubviewer.main.activity.ContentsActivity
import com.github.libliboom.epubviewer.main.activity.SettingsActivity
import com.github.libliboom.epubviewer.util.file.StorageManager
import com.github.libliboom.utils.io.FileUtils
import javax.inject.Inject

class EPubReaderViewModel : ViewModel {

    val currentBook: String = ""

    var currentChapter = 0
    var chapterSize = 0

    lateinit var ePub: EPub
    lateinit var ePubFile: String

    private val chapters = mutableListOf<String>()
    private val decompressedRootPath = EXTRACTED_EPUB_FILE_PATH

    @Inject
    constructor()

    fun startContentsActivity(context: Context) {
        val href = ePub.opf.guide.href
        val ncx = ePub.opf.ncx
        val chapters = loadChapters(ncx)
        context.startActivity(ContentsActivity.newIntent(context, href, chapters))
    }

    fun startSettingActivity(context: Context) {
        context.startActivity(SettingsActivity.newIntent(context))
    }

    fun getPath(context: Context, next: Int): String {
        currentChapter = adjustmentChapter(next)
        val root = StorageManager.getDir(context)
        val fileName = FileUtils.getFileName(ePubFile)
        return root + EXTRACTED_EPUB_FILE_PATH + FileUtils.convertToPath(fileName) + OEBPS_PATH + chapters[currentChapter].split("#")[0]
    }

    fun initEpub(context: Context) {
        val dir = StorageManager.getDir(context)
        val eFile = dir + ePubFile
        val dPath = dir + decompressedRootPath + FileUtils.getFileName(ePubFile)

        ePub = EPub(eFile, dPath)
        initChapter(ePub!!)
        chapterSize = chapters.size
    }

    private fun loadChapters(ncx: NavigationControlXml): ArrayList<String> {
        return ArrayList<String>().also {
            for (point in ncx.navMap.iterator()) {
                it += point.value.navlabelText
            }
        }
    }

    private fun adjustmentChapter(next: Int): Int {
        val lastChapter = chapterSize - 1
        return when {
            (next < 0) -> 0
            (next > lastChapter) -> lastChapter
            else -> next
        }
    }

    private fun initChapter(ePub: EPub) {
        for (s in ePub.opf.ncx.navMap.iterator()) {
            chapters.add(s.value.contentSrc)
        }
    }
}