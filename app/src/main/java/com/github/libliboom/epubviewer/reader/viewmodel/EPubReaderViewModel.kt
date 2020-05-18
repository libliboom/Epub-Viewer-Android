package com.github.libliboom.epubviewer.reader.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.libliboom.epub.EPub
import com.github.libliboom.epub.outline.opf.NavigationControlXml
import com.github.libliboom.epubviewer.dev.EPubFileStub.EXTRACTED_EPUB_FILE_PATH
import com.github.libliboom.epubviewer.dev.EPubFileStub.OEBPS_PATH
import com.github.libliboom.epubviewer.main.activity.ContentsActivity
import com.github.libliboom.epubviewer.main.activity.SettingsActivity
import com.github.libliboom.epubviewer.util.file.EPubUtils
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

    fun startContentsActivity(activity: Activity) {
        val cover = EPubUtils.getCover(ePub)
        val chapters = fetchChapters(EPubUtils.getNcx(ePub))
        activity.startActivityForResult(
            ContentsActivity.newIntent(activity, cover, chapters),
            REQUEST_CODE_CHAPTER
        )
    }

    fun startSettingActivity(context: Context) {
        context.startActivity(SettingsActivity.newIntent(context))
    }

    // TODO: 2020/05/18 insert absolute path at initialization
    fun getPath(context: Context, next: Int): String {
        currentChapter = adjustmentChapter(next)
        val root = StorageManager.getDir(context)
        val fileName = FileUtils.getFileName(ePubFile)
        val oebpsDir =
            root + EXTRACTED_EPUB_FILE_PATH + FileUtils.convertToPath(fileName) + OEBPS_PATH
        return oebpsDir + EPubUtils.getContentsSrcFileName(chapters[currentChapter])
    }

    fun initEpub(context: Context) {
        val dir = StorageManager.getDir(context)
        val eFile = dir + ePubFile
        val dPath = dir + decompressedRootPath + FileUtils.getFileName(ePubFile)

        ePub = EPub(eFile, dPath)
        initChapter(ePub!!)
        chapterSize = chapters.size
    }

    private fun fetchChapters(ncx: NavigationControlXml): ArrayList<String> {
        return ArrayList<String>().also {
            for (point in ncx.navMap.iterator()) {
                it += point.value.navlabelText
            }
        }
    }

    private fun fetchSrc(ncx: NavigationControlXml): ArrayList<String> {
        return ArrayList<String>().also {
            for (point in ncx.navMap.iterator()) {
                it += point.value.contentSrc
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
        for (p in EPubUtils.getNavMap(ePub).iterator()) {
            chapters.add(p.value.contentSrc)
        }
    }

    companion object {
        const val REQUEST_CODE_CHAPTER = 0x1000
    }
}