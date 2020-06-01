package com.github.libliboom.epubviewer.reader.viewmodel

import android.app.Activity
import android.content.Context
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.libliboom.epub.EPub
import com.github.libliboom.epub.outline.opf.NavigationControlXml
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.dev.EPubFileStub.EXTRACTED_EPUB_FILE_PATH
import com.github.libliboom.epubviewer.main.activity.ContentsActivity
import com.github.libliboom.epubviewer.main.activity.SettingsActivity
import com.github.libliboom.epubviewer.reader.view.ReaderMeasureFragment
import com.github.libliboom.epubviewer.util.file.EPubUtils
import com.github.libliboom.epubviewer.util.file.StorageManager
import com.github.libliboom.epubviewer.util.ui.TranslationUtils
import com.github.libliboom.utils.io.FileUtils
import com.github.libliboom.utils.io.robinary.PageRoBinary.Companion.PAGE_CHAR_SIZE
import com.github.libliboom.utils.parser.HtmlParser
import javax.inject.Inject

class EPubReaderViewModel : ViewModel {

    var currentPageIdx = MutableLiveData(0)
    // TODO: 2020/05/27 Wrap it up as class later
    var pageCountByRendering = MutableLiveData(0)
    var pageLock = true

    val pages4ChapterByRendering = mutableListOf<Pair<Int, Int>>()

    lateinit var ePub: EPub
    lateinit var ePubFilePath: String
    lateinit var hostActivity: FragmentActivity

    private val chapters = mutableListOf<String>()
    private var spines = mutableListOf<String>()
    private val decompressedRootPath = EXTRACTED_EPUB_FILE_PATH

    private var currentChapterIdx = 0
    private var currentSpineIdx = 0
    private var chapterSize = 0

    @Inject
    constructor()

    fun startContentsActivity(activity: Activity) {
        val cover = EPubUtils.getCover(ePub)
        val chapters = fetchChapters(EPubUtils.getNcx(ePub))
        val srcs = fetchSrc(EPubUtils.getNcx(ePub))
        activity.startActivityForResult(
            ContentsActivity.newIntent(activity, cover, chapters, srcs), REQUEST_CODE_CHAPTER
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
        initSpines()
        chapterSize = chapters.size
    }

    fun calcPageCount(activity: FragmentActivity) {
        hostActivity = activity
        val filelist = ArrayList<String>()
        for (src in ePub.opf.ncx.navMap.values) {
            filelist += (ePub.opf.oebpsPath + src.contentSrc)
        }

        activity.supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_fragment, ReaderMeasureFragment.newInstance(filelist))
            .addToBackStack(null)
            .commit()
    }

    // FIXME: 2020/05/31 on loading contents
    fun onBackPressed() {
        //hostActivity.onBackPressed()
    }

    fun getPageCount(): MutableLiveData<Int> {
        return pageCountByRendering
    }

    fun updateChapterIndex(url: String) {
        val navMap = EPubUtils.getNavMap(ePub)
        for ((idx, p) in navMap.values.withIndex()) {
            val srcFileName = p.contentSrc
            if (url.contains(srcFileName)) {
                currentChapterIdx = idx
                break
            }
        }
    }

    fun updateChapterIndex(page: Int) {
        if (ePub.pagination.pages4Chapter.size == 0) return
        val p = ePub.pagination.getSpineWithNth(page)
        currentChapterIdx = adjustmentChapter(p.first)
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

    fun loadChapterByAbsolutePath(context: Context, webView: WebView, url: String) {
        val path = getSpinePath(context, url)
        val uri = FileUtils.getFileUri(path)
        webView.loadUrl(uri)
    }

    fun loadChapterByUrl(context: Context, webView: WebView, url: String) {
        val path = FileUtils.removeFileUri(url)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileName(path))
        currentPageIdx.value = p.second
        loadPageByIndex(context, webView, p.second)
    }

    fun loadPreviousSpine(context: Context, webView: WebView) {
        val prev = adjustmentSpine(--currentSpineIdx)
        loadSpineByIndex(context, webView, prev)
    }

    fun loadNextSpine(context: Context, webView: WebView) {
        val next = adjustmentSpine(++currentSpineIdx)
        loadSpineByIndex(context, webView, next)
    }

    fun loadSpineByIndex(context: Context, webView: WebView, idx: Int) {
        currentSpineIdx = adjustmentSpine(idx)
        val path = getSpinePath(context, currentSpineIdx)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileName(path))
        loadPage(webView, path, 0)
    }

    fun unlockPaging() {
        pageLock = false
    }

    fun updatePageIndex(context: Context, url: String, nth: Int) {
        val spine = FileUtils.getFileName(FileUtils.removeFileUri(url)).split("/OEBPS/")[1]
        val idx = getSpineIndex(spine)
        val path = getSpinePath(context, idx)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileName(path))
        currentSpineIdx = idx
        currentPageIdx.value = p.second + nth
    }

    fun loadPageByIndex(context: Context, webView: WebView, page: Int) {
        val p = ePub.pagination.getSpineWithNth(page)
        currentSpineIdx = p.first
        val path = getSpinePath(context, p.first)
        loadPage(webView, path, p.second)
    }

    private fun loadPage(webView: WebView, path: String, nth: Int) {
        webView.loadUrl(FileUtils.getFileUri(path) + "${EPubUtils.DELIMITER_NTH}$nth")
    }

    // TODO: 2020/05/18 insert absolute path at initialization
    private fun getSpinePath(context: Context, next: Int): String {
        val decompressPath = StorageManager.getExtractedPath(context)
        val oebpsDir = FileUtils.getExtractedOebpsPath(decompressPath, ePubFilePath)
        val srcPath = spines[next]
        return oebpsDir + srcPath
    }

    // TODO: 2020/05/18 insert absolute path at initialization
    private fun getSpinePath(context: Context, srcPath: String): String {
        val decompressPath = StorageManager.getExtractedPath(context)
        val oebpsDir = FileUtils.getExtractedOebpsPath(decompressPath, ePubFilePath)
        return oebpsDir + srcPath
    }

    private fun getSpineIndex(spineName: String): Int {
        for ((idx, name) in spines.withIndex()) {
            if (name == spineName) return idx
        }

        return 0
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

    private fun fetchSpines(ncx: NavigationControlXml): List<String> {
        return ArrayList<String>().also {
            for (point in ncx.navMap.iterator()) {
                it += point.value.navlabelText
            }
        }.distinct()
    }

    private fun adjustmentSpine(next: Int): Int {
        val lastSpine = spines.size - 1
        return when {
            (next < 0) -> 0
            (next > lastSpine) -> lastSpine
            else -> next
        }
    }

    private fun adjustmentChapter(next: Int): Int {
        val lastChapter = chapters.size - 1
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

    private fun initSpines() {
        val list = mutableListOf<String>()
        for (f in chapters) {
            list.add(FileUtils.getFileName(f))
        }
        spines = list.distinct().toMutableList()
    }

    companion object {
        const val REQUEST_CODE_CHAPTER = 0x1000
        const val REQUEST_CODE_VIEW_MODE = 0x2000
        val parser = HtmlParser()
    }
}