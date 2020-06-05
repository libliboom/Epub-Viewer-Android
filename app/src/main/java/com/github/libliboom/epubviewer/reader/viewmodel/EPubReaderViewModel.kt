package com.github.libliboom.epubviewer.reader.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.libliboom.epub.EPub
import com.github.libliboom.epub.outline.opf.NavigationControlXml
import com.github.libliboom.epubviewer.R
import com.github.libliboom.epubviewer.db.preference.SettingsPreference
import com.github.libliboom.epubviewer.db.room.Book
import com.github.libliboom.epubviewer.db.room.BookRepository
import com.github.libliboom.epubviewer.db.room.BookRoomDatabase
import com.github.libliboom.epubviewer.dev.EPubFileStub.EXTRACTED_EPUB_FILE_PATH
import com.github.libliboom.epubviewer.main.activity.ContentsActivity
import com.github.libliboom.epubviewer.main.activity.SettingsActivity
import com.github.libliboom.epubviewer.reader.view.ReaderMeasureFragment
import com.github.libliboom.epubviewer.util.file.EPubUtils
import com.github.libliboom.epubviewer.util.file.StorageManager
import com.github.libliboom.epubviewer.util.ui.TranslationUtils
import com.github.libliboom.utils.io.FileUtils
import com.github.libliboom.utils.parser.HtmlParser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class EPubReaderViewModel : ViewModel, LifecycleObserver {

    var currentPageIdx = MutableLiveData(0)
    // TODO: 2020/05/27 Wrap it up as class later
    var pageCountByRendering = MutableLiveData(0)
    var pageLock = false
    var pages4ChapterByRendering = mutableListOf<Pair<Int, Int>>()

    lateinit var ePub: EPub
    lateinit var ePubFilePath: String
    lateinit var hostActivity: FragmentActivity

    private val chapters = mutableListOf<String>()
    private val decompressedRootPath = EXTRACTED_EPUB_FILE_PATH

    private var spines = mutableListOf<String>()
    private var currentChapterIdx = 0
    private var currentSpineIdx = 0
    private var chapterSize = 0

    private lateinit var repository: BookRepository
    private lateinit var allBooks: LiveData<List<Book>>

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

    fun cached(context: Context): Boolean {
        allBooks.value?.let {
            for (book in allBooks.value!!.iterator()) {
                val m = EPubUtils.getMode(SettingsPreference.getViewMode(context))
                if (book.config == "${FileUtils.getFileName(ePubFilePath)}-$m") {
                    val gson = Gson()
                    val mapType = object : TypeToken<Map<Int, Int>>() {}.type
                    var tutorialMap: Map<Int, Int> = gson.fromJson(book.chapters, mapType)
                    pages4ChapterByRendering = tutorialMap.toList().toMutableList()
                    pageCountByRendering.value = book.page.toInt()
                    return true
                }
            }
        }
        return false
    }

    fun initDatabase(context: Context, activity: FragmentActivity) {
        val bookDao = BookRoomDatabase.getDatabase(context, viewModelScope).bookDao()
        repository = BookRepository(bookDao)
        insert(Book("", "", "")) // workaround
        allBooks = repository.allBooks
        allBooks.observe(activity, Observer {})
    }

    fun insert(book: Book) = viewModelScope.launch {
        repository.insert(book)
    }

    fun delete(book: Book) = viewModelScope.launch {
        repository.delete(book)
    }

    fun initEpub(context: Context) {
        val booksPath = StorageManager.getBooksPath(context)
        val ePath = booksPath + ePubFilePath
        val extractedPath = StorageManager.getExtractedPath(context)
        val dPath = extractedPath + FileUtils.getFileNameFromUri(ePubFilePath) + File.separator

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
            .add(
                R.id.reader_frame_layout,
                ReaderMeasureFragment.newInstance(
                    filelist, FileUtils.getFileName(ePubFilePath)
                )
            )
            .addToBackStack(null)
            .commit()
    }

    // FIXME: 2020/05/31 on loading contents
    fun onBackPressed() {
        // hostActivity.onBackPressed()
    }

    fun getPageCount() = pageCountByRendering

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
        return TranslationUtils.run {
            when (n) {
                EFFECT_NONE -> effectNone()
                EFFECT_CUBE_OUT_DEPTH -> effectCubeOutDepth()
                EFFECT_ZOOM_OUT_PAGE -> effectZoomOutPageEffect()
                EFFECT_GEO -> effectGeo()
                EFFECT_FADE_OUT -> effectFadeOut()
                else -> effectNone()
            }
        }
    }

    fun loadChapterByAbsolutePath(context: Context, webView: WebView, url: String) {
        val path = getSpinePath(context, url)
        val uri = FileUtils.getFileUri(path)
        webView.loadUrl(uri)
    }

    fun loadChapterByUrl(context: Context, webView: WebView, url: String) {
        val path = FileUtils.removeFileUri(url)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileNameFromUri(path))
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
        loadPage(webView, path, 0)
    }

    fun loadCurrentSpine(context: Context, webView: WebView) {
        Log.d("DEV", "current: $currentSpineIdx")
        val path = getSpinePath(context, currentSpineIdx)
        loadPage(webView, path, 0)
    }

    fun unlockPaging() {
        pageLock = false
    }

    fun updatePageIndex(context: Context, url: String, nth: Int) {
        val spine = FileUtils.getFileNameFromUri(FileUtils.removeFileUri(url)).split("/OEBPS/")[1]
        val idx = getSpineIndex(spine)
        val path = getSpinePath(context, idx)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileNameFromUri(path))
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
            list.add(FileUtils.getFileNameFromUri(f))
        }
        spines = list.distinct().toMutableList()
    }

    companion object {
        const val REQUEST_CODE_CHAPTER = 0x1000
        const val REQUEST_CODE_VIEW_MODE = 0x2000
        val parser = HtmlParser()
    }
}
