package com.github.libliboom.epubviewer.reader.viewmodel

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
import com.github.libliboom.epubviewer.db.room.Book
import com.github.libliboom.epubviewer.db.room.BookDao
import com.github.libliboom.epubviewer.db.room.BookRepository
import com.github.libliboom.epubviewer.dev.EPubFileStub.EXTRACTED_EPUB_FILE_PATH
import com.github.libliboom.epubviewer.reader.view.ReaderMeasureFragment
import com.github.libliboom.epubviewer.util.file.EPubUtils
import com.github.libliboom.utils.io.FileUtils
import com.github.libliboom.utils.parser.HtmlParser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class EPubReaderViewModel @Inject constructor() : ViewModel(), LifecycleObserver {

    var currentPageIdx = MutableLiveData(0)
    // TODO: 2020/05/27 Wrap it up as class later
    var pageCountByRendering = MutableLiveData(0)
    var pageLock = false
    var pages4ChapterByRendering = mutableListOf<Pair<Int, Int>>()

    lateinit var ePub: EPub
    lateinit var ePubFilePath: String

    private val chapters = mutableListOf<String>()
    private val decompressedRootPath = EXTRACTED_EPUB_FILE_PATH

    private var spines = mutableListOf<String>()
    private var currentChapterIdx = 0
    private var currentSpineIdx = 0
    private var chapterSize = 0

    private lateinit var hostActivity: FragmentActivity
    private lateinit var repository: BookRepository
    private lateinit var allBooks: LiveData<List<Book>>

    fun cached(pageMode: Boolean): Boolean {
        allBooks.value?.let {
            for (book in allBooks.value!!.iterator()) {
                val m = EPubUtils.getMode(pageMode)
                if (book.config == "${FileUtils.getFileName(ePubFilePath)}-$m") {
                    val mapType = object : TypeToken<Map<Int, Int>>() {}.type
                    val tutorialMap: Map<Int, Int> = Gson().fromJson(book.chapters, mapType)
                    pages4ChapterByRendering = tutorialMap.toList().toMutableList()
                    pageCountByRendering.value = book.page.toInt()
                    return true
                }
            }
        }
        return false
    }

    fun initDatabase(bookDao: BookDao, activity: FragmentActivity) {
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

    fun initEpub(booksPath: String, extractedPath: String) {
        val ePath = booksPath + ePubFilePath
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

    fun loadChapterByAbsolutePath(decompressPath: String, url: String): String {
        val path = getSpinePath(decompressPath, url)
        return FileUtils.getFileUri(path)
    }

    fun loadChapterByUrl(decompressPath: String, url: String): Pair<String, Int> {
        val path = FileUtils.removeFileUri(url)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileNameFromUri(path))
        currentPageIdx.value = p.second
        return loadPageByIndex(decompressPath, p.second)
    }

    fun loadPreviousSpine(decompressPath: String): Pair<String, Int> {
        val prev = adjustmentSpine(--currentSpineIdx)
        return loadSpineByIndex(decompressPath, prev)
    }

    fun loadNextSpine(decompressPath: String): Pair<String, Int> {
        val next = adjustmentSpine(++currentSpineIdx)
        return loadSpineByIndex(decompressPath, next)
    }

    fun loadSpineByIndex(decompressPath: String, idx: Int): Pair<String, Int> {
        currentSpineIdx = adjustmentSpine(idx)
        val path = getSpinePath(decompressPath, currentSpineIdx)
        return Pair(path, 0)
    }

    fun unlockPaging() {
        pageLock = false
    }

    fun updatePageIndex(decompressPath: String, url: String, nth: Int) {
        val spine = FileUtils.getFileNameFromUri(FileUtils.removeFileUri(url)).split("/OEBPS/")[1]
        val idx = getSpineIndex(spine)
        val path = getSpinePath(decompressPath, idx)
        val p = ePub.pagination.getPageOfChapter(FileUtils.getFileNameFromUri(path))
        currentSpineIdx = idx
        currentPageIdx.value = p.second + nth
    }

    fun loadPageByIndex(decompressPath: String, page: Int): Pair<String, Int> {
        val p = ePub.pagination.getSpineWithNth(page)
        currentSpineIdx = p.first
        val path = getSpinePath(decompressPath, p.first)
        return Pair(path, p.second)
    }

    private fun getSpinePath(decompressPath: String, next: Int): String {
        val oebpsDir = FileUtils.getExtractedOebpsPath(decompressPath, ePubFilePath)
        val srcPath = spines[next]
        return oebpsDir + srcPath
    }

    private fun getSpinePath(decompressPath: String, srcPath: String): String {
        val oebpsDir = FileUtils.getExtractedOebpsPath(decompressPath, ePubFilePath)
        return oebpsDir + srcPath
    }

    private fun getSpineIndex(spineName: String): Int {
        for ((idx, name) in spines.withIndex()) {
            if (name == spineName) return idx
        }

        return 0
    }

    fun fetchChapters(ncx: NavigationControlXml) = ncx.navMap.map { it.value.navlabelText }

    fun fetchSrc(ncx: NavigationControlXml) = ncx.navMap.map { it.value.contentSrc }

    private fun fetchSpines(ncx: NavigationControlXml) = ncx.navMap.map { it.value.navlabelText }

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
