package com.github.libliboom.epubviewer.reader.viewmodel

import android.app.Activity
import android.content.Context
import android.view.View
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.libliboom.epub.EPub
import com.github.libliboom.epub.outline.opf.NavigationControlXml
import com.github.libliboom.epubviewer.dev.EPubFileStub.EXTRACTED_EPUB_FILE_PATH
import com.github.libliboom.epubviewer.main.activity.ContentsActivity
import com.github.libliboom.epubviewer.main.activity.SettingsActivity
import com.github.libliboom.epubviewer.util.file.EPubUtils
import com.github.libliboom.epubviewer.util.file.EPubUtils.getCustomHead
import com.github.libliboom.epubviewer.util.file.StorageManager
import com.github.libliboom.epubviewer.util.ui.TranslationUtils
import com.github.libliboom.utils.io.FileUtils
import com.github.libliboom.utils.io.robinary.PageRoBinary.Companion.PAGE_CHAR_SIZE
import com.github.libliboom.utils.parser.HtmlParser
import javax.inject.Inject

class EPubReaderViewModel : ViewModel {

    var currentChapterIdx = 0
    var currentPageIdx = MutableLiveData(0)

    lateinit var ePub: EPub
    lateinit var ePubFilePath: String

    private val chapters = mutableListOf<String>()
    private val decompressedRootPath = EXTRACTED_EPUB_FILE_PATH

    private var chapterSize = 0

    @Inject
    constructor()

    fun startContentsActivity(activity: Activity) {
        val cover = EPubUtils.getCover(ePub)
        val chapters = fetchChapters(EPubUtils.getNcx(ePub))
        activity.startActivityForResult(
            ContentsActivity.newIntent(activity, cover, chapters), REQUEST_CODE_CHAPTER
        )
    }

    fun startSettingActivity(activity: Activity) {
        activity.startActivityForResult(
            SettingsActivity.newIntent(activity), REQUEST_CODE_VIEW_MODE
        )
    }

    fun initEpub(context: Context) {
        val booksPath = StorageManager.getBooksPath(context)
        val ePath = booksPath + ePubFilePath
        val extractedPath = StorageManager.getExtractedPath(context)
        val dPath = extractedPath + FileUtils.getFileName(ePubFilePath)

        ePub = EPub(ePath, dPath).load()
        initChapter(ePub)
        chapterSize = chapters.size
    }

    fun updateChapterIndex(url: String) {
        val navMap = EPubUtils.getNavMap(ePub)
        for ((idx, p) in navMap.values.withIndex()) {
            val srcFileName = EPubUtils.getContentsSrcFileName(p.contentSrc)
            if (url.contains(srcFileName)) {
                currentChapterIdx = idx
                break
            }
        }
    }

    fun getEffect(n: Int): (page: View, position: Float) -> Unit {
        return when (n) {
            1 -> TranslationUtils.effectNone()
            2 -> TranslationUtils.effectCubeOutDepth()
            3 -> TranslationUtils.effectZoomOutPageEffect()
            4 -> TranslationUtils.effectGeo()
            5 -> TranslationUtils.effectFadeOut()
            else -> TranslationUtils.effectNone()
        }
    }

    fun loadChapterByUrl(context: Context, webView: WebView, url: String) {
        val path = FileUtils.removeFileUri(url)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileName(path))
        currentPageIdx.value = p.second
        loadPageByPageIndex(webView, p.second)
    }

    fun loadChapterByChapterIndex(context: Context, webView: WebView, idx: Int) {
        val srcFile = getSrcFile(context, idx)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileName(srcFile))
        currentPageIdx.value = p.second
        loadPageByPageIndex(webView, p.second)
    }

    fun loadPageByPageIndex(webView: WebView, page: Int) {
        loadPage(webView, page)
    }

    // TODO: 2020/05/18 insert absolute path at initialization
    private fun getSrcFile(context: Context, next: Int): String {
        currentChapterIdx = adjustmentChapter(next)
        val decompressPath = StorageManager.getExtractedPath(context)
        val oebpsDir = FileUtils.getExtractedOebpsPath(decompressPath, ePubFilePath)
        val srcPath = chapters[currentChapterIdx]
        return oebpsDir + EPubUtils.getContentsSrcFileName(srcPath)
    }

    private fun loadPage(webView: WebView, page: Int) {
        val head = getCustomHead()
        val body = ePub.pagination.getContentsOfPage(page)

        webView.loadDataWithBaseURL(
            FileUtils.getFileUri(EPubUtils.getOepbsPath(ePub)),
            head + "<p>${body}</p>",
            "text/html",
            "utf-8",
            null
        )
    }

    private fun getPoints(nth: Int, body: String): Pair<Int, Int> {
        var start = nth * PAGE_CHAR_SIZE
        var end = (nth + 1) * PAGE_CHAR_SIZE

        // FIXME: 2020/05/20 this is workaround for boundary condition
        if (end > body.length) {
            start = (nth - 1) * PAGE_CHAR_SIZE
            end = body.length
        }

        return Pair(start, end)
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
        const val REQUEST_CODE_VIEW_MODE = 0x2000
        val parser = HtmlParser()
    }
}