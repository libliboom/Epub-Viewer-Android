package com.github.libliboom.epubviewer.reader.viewmodel

import android.app.Activity
import android.content.Context
import android.webkit.WebView
import androidx.lifecycle.ViewModel
import com.github.libliboom.epub.EPub
import com.github.libliboom.epub.outline.opf.NavigationControlXml
import com.github.libliboom.epubviewer.db.preference.SettingsPreference
import com.github.libliboom.epubviewer.dev.EPubFileStub.EXTRACTED_EPUB_FILE_PATH
import com.github.libliboom.epubviewer.main.activity.ContentsActivity
import com.github.libliboom.epubviewer.main.activity.SettingsActivity
import com.github.libliboom.epubviewer.util.file.EPubUtils
import com.github.libliboom.epubviewer.util.file.StorageManager
import com.github.libliboom.utils.io.FileUtils
import com.github.libliboom.utils.io.robinary.PageRoBinary.Companion.PAGE_SIZE
import com.github.libliboom.utils.parser.HtmlParser
import javax.inject.Inject

class EPubReaderViewModel : ViewModel {

    var currentChapterIdx = 0

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
        val dir = StorageManager.getDir(context)
        val eFile = dir + ePubFilePath
        val dPath = dir + decompressedRootPath + FileUtils.getFileName(ePubFilePath)

        ePub = EPub(eFile, dPath)
        initChapter(ePub!!)
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

    fun loadChapterByUrl(context: Context, webView: WebView, url: String) {
        val path = FileUtils.removeFileUri(url)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileName(path))
        loadPageByPageIndex(context, webView, p.second)
    }

    fun loadChapterByPageIndex(context: Context, webView: WebView, next: Int) {
        val page = getPath(context, next)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileName(page))
        loadPageByPageIndex(context, webView, p.second)
    }

    fun loadPageByPageIndex(context: Context, webView: WebView, page: Int) {
        val p = ePub.pagination.getChapterWithNth(page)
        val chapter = getPath(context, p.first)

        val pageMode = SettingsPreference.getViewMode(context)
        if (pageMode) {
            cacheHead(p, chapter)
            loadPage(webView, chapter, p.second)
        } else {
            webView.loadUrl(FileUtils.getFileUri(chapter))
        }
    }

    // TODO: 2020/05/18 insert absolute path at initialization
    private fun getPath(context: Context, next: Int): String {
        currentChapterIdx = adjustmentChapter(next)
        val root = StorageManager.getDir(context)
        val decompressPath = root + decompressedRootPath
        val oebpsDir = FileUtils.getExtractedOebpsPath(decompressPath, ePubFilePath)
        val srcPath = chapters[currentChapterIdx]
        return oebpsDir + EPubUtils.getContentsSrcFileName(srcPath)
    }

    private fun cacheHead(p: Pair<Int, Int>, chapter: String) {
        if (cache4head.first != p.first) {
            val head = parser.parseHead(chapter)
            cache4head = Pair(p.first, head)
        }
    }

    private fun loadPage(webView: WebView, path: String, nth: Int) {
        val head = cache4head.second
        val body = parser.parseBody(path)
        var (start, end) = getPoints(nth, body)

        webView.loadDataWithBaseURL(
            FileUtils.getFileUri(EPubUtils.getOepbsPath(ePub)),
            head + body.substring(start, end),
            "text/html",
            "utf-8",
            null
        )
    }

    private var cache4head = Pair(-1, "")

    private fun getPoints(nth: Int, body: String): Pair<Int, Int> {
        var start = nth * PAGE_SIZE
        var end = (nth + 1) * PAGE_SIZE

        // FIXME: 2020/05/20 this is workaround for boundary condition
        if (end > body.length) {
            start = (nth - 1) * PAGE_SIZE
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